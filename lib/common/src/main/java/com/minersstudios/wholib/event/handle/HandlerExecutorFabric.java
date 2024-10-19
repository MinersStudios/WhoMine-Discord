package com.minersstudios.wholib.event.handle;

import com.minersstudios.wholib.event.EventContainer;
import com.minersstudios.wholib.event.EventListener;
import com.minersstudios.wholib.listener.handler.HandlerParams;
import com.minersstudios.wholib.order.Order;
import com.minersstudios.wholib.throwable.ListenerException;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

final class HandlerExecutorFabric {
    //<editor-fold desc="Constants" defaultstate="collapsed">
    private static final String HANDLER_NAME_TEMPLATE      = HandlerExecutor.class.getName() + "$Generated$";
    private static final String HANDLER_INTERNAL_NAME      = Type.getInternalName(HandlerExecutor.class);
    private static final String HANDLER_IMPL_INTERNAL_NAME = Type.getInternalName(HandlerExecutorImpl.class);
    private static final String CONSTRUCTOR_NAME           = "<init>";
    private static final String CONSTRUCTOR_DESCRIPTOR     = '(' + HandlerParams.class.descriptorString() + ")V";
    private static final String METHOD_NAME                = "execute";
    private static final String METHOD_DESCRIPTOR =
            '(' +
            EventListener.class.descriptorString() +
            EventContainer.class.descriptorString() +
            ")V";
    //</editor-fold>

    private String className;
    private ExecutorClassLoader classLoader;
    private Class<? extends HandlerExecutor<?>> clazz;

    public @NotNull HandlerExecutorFabric defineClassName(final @NotNull String className) {
        this.className = className;

        return this;
    }

    public @NotNull HandlerExecutorFabric defineLoader(final @NotNull ClassLoader loader) {
        this.classLoader = new ExecutorClassLoader(loader);

        return this;
    }

    @SuppressWarnings("unchecked")
    public @NotNull HandlerExecutorFabric generateClass(final @NotNull Method method) throws IllegalStateException, ListenerException {
        if (this.className == null) {
            throw new IllegalStateException("Class name is not defined");
        }

        if (this.classLoader == null) {
            throw new IllegalStateException("Class loader is not defined");
        }

        try {
            this.clazz = (Class<? extends HandlerExecutor<?>>)
                    this.classLoader
                    .loadClass(this.className)
                    .asSubclass(HandlerExecutorImpl.class);
        } catch (final ClassNotFoundException e) {
            this.clazz = null;
        } catch (final ClassCastException e) {
            throw new ListenerException("Class is already generated, but not an instance of " + HandlerExecutorImpl.class, e);
        }

        if (this.clazz == null) {
            this.clazz =
                    this.classLoader.defineClass(
                            this.className,
                            this.generateClassData(method)
                    );
        }

        return this;
    }

    public @NotNull HandlerExecutor<?> createInstance(final HandlerParams<?> params) throws IllegalStateException, ListenerException {
        if (this.clazz == null) {
            throw new IllegalStateException("Class is not generated");
        }

        try {
            return this.clazz.getConstructor(HandlerParams.class)
                             .newInstance(params);
        } catch (final Throwable e) {
            throw new ListenerException("Failed to create instance of " + this.clazz, e);
        }
    }

    private void generateConstructor(final @NotNull ClassWriter writer) {
        final GeneratorAdapter generator =
                new GeneratorAdapter(
                        writer.visitMethod(ACC_PUBLIC, CONSTRUCTOR_NAME, CONSTRUCTOR_DESCRIPTOR, null, null),
                        ACC_PUBLIC,
                        CONSTRUCTOR_NAME,
                        CONSTRUCTOR_DESCRIPTOR
                );

        generator.loadThis();
        generator.loadArg(0);
        generator.visitMethodInsn(
                INVOKESPECIAL, HANDLER_IMPL_INTERNAL_NAME,
                CONSTRUCTOR_NAME,
                CONSTRUCTOR_DESCRIPTOR,
                false
        );
        generator.returnValue();
        generator.endMethod();
    }

    private void generateMethod(
            final @NotNull ClassWriter writer,
            final @NotNull Method method
    ) {
        final var declaringClass = method.getDeclaringClass();
        final boolean isInterface = declaringClass.isInterface();
        final GeneratorAdapter generator =
                new GeneratorAdapter(
                        writer.visitMethod(ACC_PUBLIC, METHOD_NAME, METHOD_DESCRIPTOR, null, null),
                        ACC_PUBLIC,
                        METHOD_NAME,
                        METHOD_DESCRIPTOR
                );

        generator.loadArg(0);
        generator.checkCast(Type.getType(declaringClass));

        generator.loadArg(1);
        generator.checkCast(Type.getType(method.getParameterTypes()[0]));

        generator.visitMethodInsn(
                isInterface
                ? INVOKEINTERFACE
                : INVOKEVIRTUAL,
                Type.getInternalName(declaringClass),
                method.getName(),
                Type.getMethodDescriptor(method),
                isInterface
        );
        generator.returnValue();
        generator.endMethod();
    }

    private byte @NotNull [] generateClassData(final @NotNull Method method) {
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        writer.visit(
                V21,
                ACC_PUBLIC,
                this.className.replace('.', '/'),
                null, HANDLER_IMPL_INTERNAL_NAME,
                new String[] { HANDLER_INTERNAL_NAME }
        );
        this.generateConstructor(writer);
        this.generateMethod(writer, method);
        writer.visitEnd();

        return writer.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public static <O extends Order<O>> @NotNull HandlerExecutor<O> create(
            final @NotNull Method method,
            final @NotNull HandlerParams<O> handlerParams
    ) throws IllegalStateException, ListenerException {
        return (HandlerExecutor<O>) new HandlerExecutorFabric()
                .defineClassName(HANDLER_NAME_TEMPLATE + method.hashCode())
                .defineLoader(method.getDeclaringClass().getClassLoader())
                .generateClass(method)
                .createInstance(handlerParams);
    }

    private static final class ExecutorClassLoader extends ClassLoader {

        static {
            registerAsParallelCapable();
        }

        ExecutorClassLoader(final @NotNull ClassLoader parent) {
            super(parent);
        }

        @SuppressWarnings("unchecked")
        public @NotNull Class<? extends HandlerExecutorImpl<?>> defineClass(
                final @NotNull String name,
                final byte @NotNull [] bytes
        ) {
            final var clazz = this.defineClass(name, bytes, 0, bytes.length);

            this.resolveClass(clazz);

            return (Class<? extends HandlerExecutorImpl<?>>) clazz.asSubclass(HandlerExecutorImpl.class);
        }
    }
}

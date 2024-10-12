package com.minersstudios.whomine.api.event.handle;

import com.minersstudios.whomine.api.event.EventContainer;
import com.minersstudios.whomine.api.event.EventListener;
import com.minersstudios.whomine.api.throwable.ListenerException;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.*;

final class EventExecutorFabric {
    private static final String NAME_TEMPLATE = EventExecutor.class.getName() + "$Generated$";
    private static final String METHOD_NAME = "execute";
    private static final String METHOD_DESCRIPTOR =
            "(L" + Type.getInternalName(EventListener.class) +
            ";L" + Type.getInternalName(EventContainer.class) + ";)V";

    private String className;
    private ExecutorClassLoader classLoader;
    private Class<? extends EventExecutor> clazz;

    public @NotNull EventExecutorFabric defineClassName(final @NotNull String className) {
        this.className = className;

        return this;
    }

    public @NotNull EventExecutorFabric defineLoader(final @NotNull ClassLoader loader) {
        this.classLoader = new ExecutorClassLoader(loader);

        return this;
    }

    public @NotNull EventExecutorFabric generateClass(final @NotNull Method method) throws IllegalStateException, ListenerException {
        if (this.className == null) {
            throw new IllegalStateException("Class name is not defined");
        }

        if (this.classLoader == null) {
            throw new IllegalStateException("Class loader is not defined");
        }

        try {
            this.clazz =
                    this.classLoader
                    .loadClass(this.className)
                    .asSubclass(EventExecutor.class);
        } catch (final ClassNotFoundException e) {
            this.clazz = null;
        } catch (final ClassCastException e) {
            throw new ListenerException("Class is already generated, but not an instance of EventExecutor", e);
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

    public @NotNull EventExecutor createInstance() throws IllegalStateException, ListenerException {
        if (this.clazz == null) {
            throw new IllegalStateException("Class is not generated");
        }

        try {
            return this.clazz.getConstructor().newInstance();
        } catch (final Throwable e) {
            throw new ListenerException("Failed to create instance of " + this.clazz, e);
        }
    }

    private void generateConstructor(final @NotNull ClassWriter writer) {
        final GeneratorAdapter generator =
                new GeneratorAdapter(
                        writer.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null),
                        ACC_PUBLIC,
                        "<init>",
                        "()V"
                );

        generator.loadThis();
        generator.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false);
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
        final String className = this.className.replace('.', '/');
        final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        writer.visit(
                V21,
                ACC_PUBLIC,
                className,
                null,
                Type.getInternalName(Object.class),
                new String[] { Type.getInternalName(EventExecutor.class) }
        );
        this.generateConstructor(writer);
        this.generateMethod(writer, method);
        writer.visitEnd();

        return writer.toByteArray();
    }

    public static @NotNull EventExecutor create(final @NotNull Method method) throws IllegalStateException, ListenerException {
        return new EventExecutorFabric()
                .defineClassName(NAME_TEMPLATE + method.hashCode())
                .defineLoader(method.getDeclaringClass().getClassLoader())
                .generateClass(method)
                .createInstance();
    }

    private static final class ExecutorClassLoader extends ClassLoader {

        static {
            registerAsParallelCapable();
        }

        ExecutorClassLoader(final @NotNull ClassLoader parent) {
            super(parent);
        }

        public @NotNull Class<? extends EventExecutor> defineClass(
                final @NotNull String name,
                final byte @NotNull [] bytes
        ) {
            final var clazz = this.defineClass(name, bytes, 0, bytes.length);

            this.resolveClass(clazz);

            return clazz.asSubclass(EventExecutor.class);
        }
    }
}

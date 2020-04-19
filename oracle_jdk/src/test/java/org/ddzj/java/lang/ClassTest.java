package org.ddzj.java.lang;

import org.ddzj.base.Person;
import org.junit.Test;

import javax.xml.ws.Service;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ClassTest {

    @Test
    public void toStringTest() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("org.ddzj.java.lang.ClassTest");
        System.out.println(aClass.toString());
    }

    @Test
    public void toGenericString() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("org.ddzj.java.lang.ClassTest");
        System.out.println(aClass.toGenericString());
    }

    @Test
    public void forName() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("java.lang.Class");
    }

    @Test
    public void forName1() {
    }

    @Test
    public void newInstance() throws Exception {
        Class<?> aClass = Class.forName("org.ddzj.base.Person");
        Person p = (Person)aClass.newInstance();
        System.out.println(p.toString());
    }

    @Test
    public void isInstance() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("org.ddzj.base.Person");
        Class<?> oClass = Class.forName("java.lang.Object");
        System.out.println(oClass.isInstance(aClass));
    }

    @Test
    public void isAssignableFrom() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("org.ddzj.base.Person");
        Class<?> oClass = Class.forName("java.lang.Object");
        System.out.println(oClass.isAssignableFrom(aClass));
    }

    @Test
    public void isInterface() throws ClassNotFoundException {
        Class<?> cClass = Class.forName("java.lang.Cloneable");
        System.out.println(cClass.isInterface());
    }

    @Test
    public void isArray() {
        String[] strs = {"a","b"};
        Class<? extends String[]> aClass = strs.getClass();
        System.out.println(aClass.isArray());

        List<Object> l1 = new LinkedList<>();
        List<Object> l2 = new LinkedList<>();
        Class<? extends List> aClass1 = l1.getClass();
        Class<? extends List> bClass1 = l2.getClass();
        System.out.println(aClass1.isArray());
        System.out.println(bClass1.isArray());

        int[] is = {1,2,3};
        Class<? extends int[]> aClass2 = is.getClass();
        System.out.println(aClass2.isArray());

        BigDecimal[] bigs = {new BigDecimal(1),new BigDecimal(2)};
        Class<? extends BigDecimal[]> aClass3 = bigs.getClass();
        System.out.println(aClass3.isArray());
    }

    @Test
    public void isPrimitive() {
        Object obj = new Object();
        Class<?> aClass = obj.getClass();
        System.out.println(aClass.isPrimitive()); //false

        Class<BigDecimal> bigDecimalClass = BigDecimal.class;
        System.out.println(bigDecimalClass.isPrimitive()); //false

        Class<? extends Double> aClass2 = Double.class;
        Class<Double> type = Double.TYPE;
        System.out.println(aClass2.isPrimitive()); //false
        System.out.println(type);

        Class<Double> doubleClass = double.class;
        System.out.println(doubleClass.isPrimitive()); //true
    }

    @Test
    public void isAnnotation() throws ClassNotFoundException {
        Class<?> aClass = Class.forName("sun.reflect.CallerSensitive");
        System.out.println(aClass.isAnnotation());
    }

    @Test
    public void isSynthetic() throws ClassNotFoundException {
        Class<?> aClass2 = Class.forName("java.lang.ClassLoader");
        Person p = new Person();
        Class<? extends Person> aClass = p.getClass();
        Class<? extends Class> aClass1 = aClass2.getClass();
        System.out.println(aClass.isSynthetic());
        System.out.println(aClass1.isSynthetic());
    }

    @Test
    public void getName() {
        String name = double.class.getName();
        String name1 = Person.class.getName();
        System.out.println(name);
        System.out.println(name1);
    }

    @Test
    public void getClassLoader() {
        ClassLoader classLoader = Person.class.getClassLoader();
        System.out.println(classLoader.toString());
    }

    @Test
    public void getClassLoader0() {
    }

    @Test
    public void getTypeParameters() {
        TypeVariable[] tValue = List.class.getTypeParameters();
        System.out.println(tValue[0].getName());
        for (TypeVariable typeVariable : tValue) {
            System.out.println(typeVariable.getName());
        }
    }

    @Test
    public void getSuperclass() {
        Class<? super Person> superclass = Person.class.getSuperclass();
        System.out.println(superclass.getName());

        String name = Class.class.getSuperclass().getName();
        System.out.println(name);

        System.out.println("======================NullPointerException========================");
        String name1 = Object.class.getSuperclass().getName();
        Class<? super Cloneable> superclass1 = Cloneable.class.getSuperclass();
        System.out.println(name1);
        System.out.println(superclass1.getName());
    }

    @Test
    public void getGenericSuperclass() {
        Type genericSuperclass = Person.class.getGenericSuperclass();
        System.out.println(genericSuperclass); //class java.lang.Object

        Type genericSuperclass1 = Class.class.getSuperclass().getGenericSuperclass();
        System.out.println(genericSuperclass1); //null
    }

    @Test
    public void getPackage() {
        String name = Person.class.getPackage().getName();
        System.out.println(name);
    }

    @Test
    public void getInterfaces() {
        Class<?>[] interfaces = Person.class.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println(anInterface.getName());
        }
    }

    @Test
    public void getGenericInterfaces() {
        Type[] genericInterfaces = Person.class.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            System.out.println(genericInterface.getTypeName());
        }
    }

    @Test
    public void getComponentType() {
        Class<?> componentType1 = int[].class.getComponentType();
        System.out.println(componentType1.getName()); //int

        Class<?> componentType = Person.class.getComponentType();
        System.out.println( componentType.getName()); //NullPointerException
    }

    @Test
    public void getModifiers() {
        int modifiers = Person.class.getModifiers();
        System.out.println(modifiers);
    }

    @Test
    public void getSigners() {
        Object[] signers1 = Object.class.getSigners();
        for (Object o : signers1) {
            System.out.println(o.toString());
        }

        Object[] signers = Person.class.getSigners();
        for (Object signer : signers) {
            System.out.println(signer.toString());
        }
    }

    @Test
    public void setSigners() {
    }

    @Test
    public void getEnclosingMethod() {
        Method enclosingMethod = Object.class.getEnclosingMethod();
        System.out.println(enclosingMethod.getName());
    }

    @Test
    public void getEnclosingConstructor() {
    }

    @Test
    public void getDeclaringClass() {
    }

    @Test
    public void getEnclosingClass() {
    }

    @Test
    public void getSimpleName() {
        String simpleName = Person.class.getSimpleName();
        System.out.println(simpleName);
    }

    @Test
    public void getTypeName() {
        String typeName = Person.class.getTypeName();
        System.out.println(typeName);
    }

    @Test
    public void getCanonicalName() {
    }

    @Test
    public void isAnonymousClass() {
    }

    @Test
    public void isLocalClass() {
        System.out.println(Person.class.isLocalClass());
        System.out.println(Object.class.isLocalClass());
    }

    @Test
    public void isMemberClass() {
    }

    @Test
    public void getClasses() {
    }

    @Test
    public void getFields() {
        Field[] fields = Person.class.getFields();
        for (Field field : fields) {
            System.out.println(field.getType()+":"+ field.getName());
        }
    }

    @Test
    public void getMethods() {
        Method[] methods = Person.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }

    @Test
    public void getConstructors() {
    }

    @Test
    public void getField() throws NoSuchFieldException {
        Field name = Person.class.getField("name");
        System.out.println(name.getName()); //NoSuchFieldException， 因为是private修改的
    }

    @Test
    public void getMethod() {
    }

    @Test
    public void getConstructor() {
    }

    @Test
    public void getDeclaredClasses() {
    }

    @Test
    public void getDeclaredFields() {
    }

    @Test
    public void getDeclaredMethods() {
    }

    @Test
    public void getDeclaredConstructors() {
    }

    @Test
    public void getDeclaredField() {
    }

    @Test
    public void getDeclaredMethod() {
    }

    @Test
    public void getDeclaredConstructor() {
    }

    @Test
    public void getResourceAsStream() {
        InputStream resourceAsStream = Person.class.getResourceAsStream("/config.properties");
        Scanner scanner = new Scanner(resourceAsStream);
        System.out.println("scanner.next-->"+scanner.next());
    }

    @Test
    public void getResource() {
        URL resource = Person.class.getResource("/config.properties");
        String file = resource.getFile();
        File file1 = new File(file);
        System.out.println(file1.exists());
    }

    @Test
    public void getProtectionDomain() {
    }

    @Test
    public void getPrimitiveClass() {
    }

    @Test
    public void getRawAnnotations() {
    }

    @Test
    public void getRawTypeAnnotations() {
    }

    @Test
    public void getExecutableTypeAnnotationBytes() {
    }

    @Test
    public void getConstantPool() {
    }

    @Test
    public void desiredAssertionStatus() {
    }

    @Test
    public void isEnum() {
        System.out.println(Service.Mode.class.isEnum());
    }

    @Test
    public void getEnumConstants() {
    }

    @Test
    public void getEnumConstantsShared() {
    }

    @Test
    public void enumConstantDirectory() {
    }

    @Test
    public void cast() {
        Object cast = Object.class.cast(new Person());
        System.out.println(cast.toString());
    }

    @Test
    public void asSubclass() {
    }

    @Test
    public void getAnnotation() {
    }

    @Test
    public void isAnnotationPresent() {
    }

    @Test
    public void getAnnotationsByType() {
    }

    @Test
    public void getAnnotations() {
    }

    @Test
    public void getDeclaredAnnotation() {
    }

    @Test
    public void getDeclaredAnnotationsByType() {
    }

    @Test
    public void getDeclaredAnnotations() {
    }

    @Test
    public void casAnnotationType() {
    }

    @Test
    public void getAnnotationType() {
    }

    @Test
    public void getDeclaredAnnotationMap() {
    }

    @Test
    public void getAnnotatedSuperclass() {
    }

    @Test
    public void getAnnotatedInterfaces() {
    }
}
package com.nhnacademy.shoppingmall.common.mvc.service;

import com.nhnacademy.shoppingmall.common.mvc.annotation.Repository;
import com.nhnacademy.shoppingmall.common.mvc.annotation.Service;
import jakarta.servlet.ServletContext;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Service, Repository의 어노테이션을 가진 객체를 관리하는 factory class
 */
@Slf4j
public class ServiceFactory {
    public static final String CONTEXT_SERVICE_FACTORY_NAME = "CONTEXT_SERVICE_FACTORY";

    private final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    public void initialize(Set<Class<?>> classes, ServletContext ctx) {
        // @Service, @Repository를 갖는 모든 클래스의 bean 생성
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Repository.class) || clazz.isAnnotationPresent(Service.class)) {
                createBean(clazz, ctx, classes);
            }
        }

        // 생성된 객체 중 @Service 어노테이션이 붙은 것들을 ServletContext에 등록
        for (Map.Entry<Class<?>, Object> entry : beanMap.entrySet()) {
            Class<?> clazz = entry.getKey();
            Object instance = entry.getValue();

            // 구체 클래스(Implementation class)인 경우에만 체크하여 등록
            if (clazz.isAnnotationPresent(Service.class)) {
                Service serviceAnnotation = clazz.getAnnotation(Service.class);
                String attributeName = serviceAnnotation.value();
                
                // 어노테이션에 설정된 이름(value)이 있으면 그 이름으로 ServletContext에 저장
                if (!attributeName.isEmpty()) {
                    ctx.setAttribute(attributeName, instance);
                    log.info("ServletContext에 서비스 등록 완료: {} (이름: {})", clazz.getName(), attributeName);
                }
            }
        }
        ctx.setAttribute(CONTEXT_SERVICE_FACTORY_NAME, this);
    }

    private Object createBean(Class<?> clazz, ServletContext ctx, Set<Class<?>> classes) {
        // 이미 생성된 적이 있다면 기존 객체를 반환
        if (beanMap.containsKey(clazz)) {
            return beanMap.get(clazz);
        }

        try {
            // 해당 클래스의 생성자 정보 가져오기
            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 0) {
                constructors = clazz.getDeclaredConstructors();
            }

            Constructor<?> constructor = constructors[0]; // 첫 번째 생성자를 사용하여 객체를 생성
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            // 생성자에 필요한 각 파라미터(의존성) 채워넣기
            for (int i = 0; i < parameterTypes.length; i++) {
                args[i] = findBean(parameterTypes[i], ctx, classes);
            }

            Object instance = constructor.newInstance(args);
            beanMap.put(clazz, instance);
            
            // 인터페이스 타입으로도 검색될 수 있도록 인터페이스 정보도 맵에 등록합니다.
            for (Class<?> iface : clazz.getInterfaces()) {
                beanMap.put(iface, instance);
            }

            return instance;
        } catch (Exception e) {
            log.error("Bean 생성 실패: {}", clazz.getName());
            throw new RuntimeException("Bean 생성 중 오류 발생: " + clazz.getName(), e);
        }
    }

    private Object findBean(Class<?> type, ServletContext ctx, Set<Class<?>> classes) {
        // 1. 이미 생성되어 beanMap에 있는지 먼저 확인합니다.
        if (beanMap.containsKey(type)) {
            return beanMap.get(type);
        }

        // 2. 전체 클래스 목록에서 해당 타입(인터페이스 등)을 구현한 클래스가 있는지 찾습니다.
        for (Class<?> clazz : classes) {
            if (type.isAssignableFrom(clazz) && !clazz.isInterface()) {
                // 찾았다면 해당 클래스의 Bean을 생성하여 반환합니다.
                return createBean(clazz, ctx, classes);
            }
        }

        // 3. 만약 beanMap에도 없고 구현체도 없다면, ServletContext에 직접 등록된 객체인지 확인합니다. (예: RequestChannel)
        if (type.getSimpleName().equals("RequestChannel")) {
            Object channel = ctx.getAttribute("requestChannel");
            if (channel != null) return channel;
        }

        // 끝내 찾지 못하면 예외를 발생시킵니다.
        throw new RuntimeException("의존성 해결 실패: " + type.getName() + " 타입의 Bean을 찾을 수 없습니다.");
    }

    /**
     * 외부에서 타입 정보를 통해 Bean을 직접 꺼낼 때 사용합니다.
     * @param type 꺼내려는 타입
     * @param <T> 타입 제네릭
     * @return Bean 객체
     */
    public <T> T getBean(Class<T> type) {
        Object bean = beanMap.get(type);
        return bean != null ? type.cast(bean) : null;
    }
}

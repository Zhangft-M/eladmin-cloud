package org.micah.mq.component;

import org.micah.mq.config.MqModel;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

import java.util.Objects;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-09-06 16:49
 **/
public class ExchangeCondition implements Condition {
    /**
     * Determine if the condition matches.
     *
     * @param context  the condition context
     * @param metadata the metadata of the {@link AnnotationMetadata class}
     *                 or {@link MethodMetadata method} being checked
     * @return {@code true} if the condition matches and the component can be registered,
     * or {@code false} to veto the annotated component's registration
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        BeanDefinition exchange = Objects.requireNonNull(context.getBeanFactory()).getBeanDefinition("exchange");
        String beanClassName = exchange.getBeanClassName();
        MqModel model = context.getEnvironment().getProperty("eladmin.mq.model", MqModel.class);
        if (model != null && beanClassName != null){
            return beanClassName.equals(model.getModel());
        }
        return false;
    }
}

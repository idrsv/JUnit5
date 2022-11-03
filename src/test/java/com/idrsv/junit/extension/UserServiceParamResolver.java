package com.idrsv.junit.extension;

import com.idrsv.junit.service.UserService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.*;

//Создается лишь один раз как singleton
public class UserServiceParamResolver implements ParameterResolver {
    @Override
    //ParameterContext - говорит всю информацию о заинжекченом объекте в будущем
    //Инжектим наш UserService в ParameterContext
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UserService.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        //Store-HashMap своего рода
        var store = extensionContext.getStore(create(UserService.class));
        return store.getOrComputeIfAbsent(UserService.class, it -> new UserService());
    }
}
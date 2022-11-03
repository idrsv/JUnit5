package com.idrsv.junit;

import com.idrsv.junit.extension.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        GlobalExtension.class,
        UserServiceParamResolver.class,
        PostProcessingExtension.class,
        ConditionalExtension.class,
//        ThrowableExtension.class
})
public class TestBase {
}

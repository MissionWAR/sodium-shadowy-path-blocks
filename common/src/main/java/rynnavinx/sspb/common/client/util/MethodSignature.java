package rynnavinx.sspb.common.client.util;

import java.lang.reflect.Method;
import java.util.Arrays;


public record MethodSignature(String name, Class<?>[] paramTypes) {

    public static MethodSignature fromMethod(Method method){
        return new MethodSignature(method.getName(), method.getParameterTypes());
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof MethodSignature otherMethodInfo)) {return false;}

        return this.name.equals(otherMethodInfo.name) &&
                Arrays.equals(this.paramTypes, otherMethodInfo.paramTypes);
    }
}

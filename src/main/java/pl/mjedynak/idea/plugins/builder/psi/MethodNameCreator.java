package pl.mjedynak.idea.plugins.builder.psi;

import static org.apache.commons.lang.StringUtils.capitalize;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.lowerCase;

public class MethodNameCreator {

    public String createMethodName(String methodPrefix, String fieldName) {
        if (isEmpty(methodPrefix)) {
            return fieldName;
        } else {
            String fieldNameUppercase = capitalize(fieldName);
            return methodPrefix + fieldNameUppercase;
        }
    }
}

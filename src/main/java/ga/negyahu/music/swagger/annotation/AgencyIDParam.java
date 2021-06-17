package ga.negyahu.music.swagger.annotation;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams(@ApiImplicitParam(name = "id", example = "1", value = "소속사 고유번호"
    , defaultValue = "1", dataTypeClass = Long.class, required = true, paramType = "path"))
public @interface AgencyIDParam {

}

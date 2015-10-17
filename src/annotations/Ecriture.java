package annotations;
import java.lang.annotation.*; 

//Is the annotation available at execution time
@Retention(RetentionPolicy.RUNTIME) 
//Annotation associated with a method
@Target(ElementType.METHOD) 


public @interface Ecriture {

}

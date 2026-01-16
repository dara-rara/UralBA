package ural.ba.project.UralBA.exepction;

/**
 * Доменное исключение "ресурс не найден" для любых сущностей
 *
 * @author Daria
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String resourceField) {
        super(resourceName + " с " + resourceField + " не существует");
    }
}
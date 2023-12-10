package sk.catsname.cookbooks.storage;

import java.io.Serial;

public class EntityNotFoundException extends  RuntimeException {
    @Serial
    private static final long serialVersionUID = -7394447883318471687L;

    public EntityNotFoundException(String message) { super(message); }
}

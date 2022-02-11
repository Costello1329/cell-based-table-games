package core;

public interface FieldFactory<Field extends AbstractField> {
    Field createField ();
}

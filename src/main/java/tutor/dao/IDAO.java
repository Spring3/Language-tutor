package tutor.dao;

/**
 * Created by user on 12.02.2015.
 */
public interface IDAO<T> {
    public boolean create(T value);
    public T read(int id);
    public boolean update(T value);
    public boolean delete(T value);

}

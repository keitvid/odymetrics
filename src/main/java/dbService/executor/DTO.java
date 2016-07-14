package dbService.executor;

/**
 * Created by IPermyakova on 07.07.2016.
 */
public class DTO <T> {
    private Boolean status;
    private T value;

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean getStatus() {
        return status;
    }

    public T getValue() {
        return value;
    }


}

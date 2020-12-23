package exception;


import java.security.KeyStoreException;

public class SpringRedittException extends RuntimeException {
    public SpringRedittException(String exceptionMessage, KeyStoreException e){
        super(exceptionMessage);
    }

}

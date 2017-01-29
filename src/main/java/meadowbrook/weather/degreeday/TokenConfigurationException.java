package meadowbrook.weather.degreeday;

/**
 * Formal means of communicating about state of supplied tokens.
 */
public class TokenConfigurationException extends RuntimeException {
    public TokenConfigurationException(String msg) {
        super(msg);
    }
}

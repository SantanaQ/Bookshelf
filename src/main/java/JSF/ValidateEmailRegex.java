package JSF;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value="validateEmailRegex")
public class ValidateEmailRegex implements Validator<Object> {

    @Override
    public void validate(FacesContext context, UIComponent component, Object email) throws ValidatorException {
		Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher((String)email);
        if (email == null) {
            return;
        }

        if (!matcher.matches()) {
            throw new ValidatorException(new FacesMessage("Die E-Mail hat nicht das richtige Format, z.B. example@service.com"));
        }
    }

}

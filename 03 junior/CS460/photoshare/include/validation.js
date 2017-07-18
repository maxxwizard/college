/* Courtesy of http://w3schools.com/js/js_form_validation.asp */

function validate_required(field, alertText)
{
	with (field)
	{
		if (value == null || value == "") {
			alert(alertText);
			return false;
		} else {
			return true;
		}
	}
}

function validate_login_form(thisForm)
{
	with (thisForm)
	{
		if (!validate_required(email, "Email field must be filled out.")) {
			email.focus();
			return false;
		}
		if (!validate_required(password, "Password field must be filled out.")) {
			password.focus();
			return false;
		}
	}
	return true;
}

function validate_reg_form(thisForm)
{
	with (thisForm)
	{
		if (!validate_required(password1, "Password field must be filled out.")) {
			password1.focus();
			return false;
		}
		if (!validate_required(password2, "Password confirmation field must be filled out.")) {
			password2.focus();
			return false;
		}
		if (!validate_required(firstName, "First name field must be filled out.")) {
			firstName.focus();
			return false;
		}
		if (!validate_required(lastName, "Last name field must be filled out.")) {
			lastName.focus();
			return false;
		}
		if (!validate_required(dob, "Date of birth field must be filled out.")) {
			dob.focus();
			return false;
		}
	}
	return true;
}

function checkPasswordMatch(theForm)
{
	with (theForm)
	{
		if (password1.value != password2.value) {
			alert("Your passwords do not match!");
			return false;
		} else {
			return true;
		}
	}
}

function validate_album_form(thisForm)
{
	with (thisForm)
	{
		if (!validate_required(name, "Album title field must be filled out.")) {
			name.focus();
			return false;
		}
	}
	return true;
}
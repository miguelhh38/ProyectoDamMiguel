package com.example.proyectodammiguel.utils;

import android.widget.EditText;

import com.example.proyectodammiguel.RegistroActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean validarName(String name) {
        if (name.length() < 2 || name.length() > 20) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean validarMail(String mail) {
        Pattern pattern = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");
        Matcher marcher = pattern.matcher(mail);
        return marcher.matches();
    }
    public static boolean validarPass(String pass) {
        if (pass.length() < 8) {
            return false;
        }
        boolean contieneMayus = false;
        boolean contieneMinus = false;
        boolean contieneNum = false;
        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (Character.isUpperCase(c)) {
                contieneMayus = true;
            }
            if (Character.isLowerCase(c)) {
                contieneMinus = true;
            }
            if (Character.isDigit(c)) {
                contieneNum = true;
            }
            if (contieneMayus && contieneMinus && contieneNum) {
                return true;
            }
        }
        return true;
    }
    public static boolean validarUser(String user) {
        String regex = "^[a-zA-Z0-9_]+$";
        return user.matches(regex);
    }

    public static boolean validarTelf(String telf) {
        String regex = "^\\d{10}$";
        return telf.matches(regex);
    }

    /**
     * Valida el número de teléfono introducido en un campo de texto y devuelve un mensaje de error si no es válido.
     *
     * @param text El campo de texto que contiene el número de teléfono a validar.
     * @return Un mensaje de error si el número de teléfono no es válido, o una cadena vacía si es válido.
     */
    public static String validarTelfString(EditText text) {
        String ret = "";
        if (Validator.validarTelf(text.getText().toString())) {
            ret += " - El número de teléfono introducido no es válido\n";
        }
        return ret;
    }



}

package com.filiera.facile.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValutaPraticaRequest {

    @NotBlank(message = "Le note di valutazione sono obbligatorie")
    @Size(max = 1000, message = "Le note non possono superare i 1000 caratteri")
    private String note;

    public ValutaPraticaRequest() {}

    public ValutaPraticaRequest(String note) {
        this.note = note;
    }

}
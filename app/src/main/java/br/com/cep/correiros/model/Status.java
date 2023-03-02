package br.com.cep.correiros.model;

public enum Status {
    NEED_SETUP,     // Precisa baixar o CSV
    SETUP_RUNNING,  // Está baixando / salvando no banco
    READY;          // serviço apto para ser consumido
}

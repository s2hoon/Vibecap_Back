package com.example.vibecap_back.global.config.storage;

import static com.example.vibecap_back.global.common.response.BaseResponseStatus.FILE_SAVE_ERROR;

public class FileSaveErrorException extends Exception {
    public FileSaveErrorException() {
        super(FILE_SAVE_ERROR.getMessage());
    }
}

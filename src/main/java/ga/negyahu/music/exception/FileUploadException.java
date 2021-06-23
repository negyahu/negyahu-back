package ga.negyahu.music.exception;

public class FileUploadException extends RuntimeException{

    public FileUploadException() {
        super("[ERROR] 파일 업로드에 실패했습니다.");
    }

    public FileUploadException(String message) {
        super(message);
    }
}

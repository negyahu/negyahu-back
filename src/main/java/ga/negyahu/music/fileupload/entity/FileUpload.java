package ga.negyahu.music.fileupload.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public interface FileUpload<T> {

    T getEntity();

    Long getFK();

}

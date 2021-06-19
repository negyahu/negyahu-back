package ga.negyahu.music.fileupload.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public interface FileUpload {

    Object getEntity();

    Long getFK();

}

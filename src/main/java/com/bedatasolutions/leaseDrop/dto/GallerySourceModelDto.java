package com.bedatasolutions.leaseDrop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.apache.tomcat.jni.FileInfo;

import java.io.File;
import java.util.List;

@Builder
public record GallerySourceModelDto(String groupName,
                                    @JsonIgnore
                                    File groupDir,
                                    @JsonIgnore
                                    FileInfoDto groupDirInfo,
                                    List<FileInfoDto> files) {
}

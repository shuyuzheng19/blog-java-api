package com.zsy.admin.entity;
import com.zsy.admin.utils.GlobalUtils;
import com.zsy.admin.vos.FileVo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import java.util.Date;

/**
 * @author 郑书宇
 * @create 2023/6/5 15:13
 * @desc
 */
@Data
@Entity
@Table(name="files")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private String oldName;
    private String newName;
    private Date date;
    private Long size;
    private String suffix;
    private String url;
    private String absolutePath;
    private Boolean isPublic;

    /*
    是否为删除状态
     */
    private boolean deleted;

    @OneToOne(targetEntity = FileMd5.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private FileMd5 fileMd5;

    public FileVo toVo(){
        FileVo fileVo=new FileVo();
        fileVo.setId(id);
        fileVo.setSizeStr(GlobalUtils.getSizeStr(size));
        fileVo.setDateStr(GlobalUtils.formatDateTime(date,"yyyy-MM-dd HH:mm"));
        fileVo.setName(oldName);
        fileVo.setMd5(fileMd5.getMd5());
        fileVo.setSuffix(suffix);
        fileVo.setUrl(url);
        return fileVo;
    }
}

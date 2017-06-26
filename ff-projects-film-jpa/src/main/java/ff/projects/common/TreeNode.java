package ff.projects.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by F on 2017/6/23.
 */
@Data
public class TreeNode implements Serializable {

    private Long id;

    private String text;

    private String state;

    private String htmlUrl; //pages/table 目标页面

    private String dataUrl; //目标数据

    private Set<TreeNode>  children;


}

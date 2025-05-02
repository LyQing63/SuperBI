package io.lyqing64.github.superbi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName chart_generate
 */
@TableName(value = "chart_generate")
@Data
public class ChartGenerate implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *
     */
    private Long fileId;
    /**
     *
     */
    private Object chartJson;
    /**
     *
     */
    private String modelUsed;
    /**
     *
     */
    private String promptUsed;
    /**
     *
     */
    private String analysis;
    /**
     *
     */
    private Date createdAt;
    /**
     *
     */
    private String status;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        ChartGenerate other = (ChartGenerate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
                && (this.getChartJson() == null ? other.getChartJson() == null : this.getChartJson().equals(other.getChartJson()))
                && (this.getModelUsed() == null ? other.getModelUsed() == null : this.getModelUsed().equals(other.getModelUsed()))
                && (this.getPromptUsed() == null ? other.getPromptUsed() == null : this.getPromptUsed().equals(other.getPromptUsed()))
                && (this.getAnalysis() == null ? other.getAnalysis() == null : this.getAnalysis().equals(other.getAnalysis()))
                && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getChartJson() == null) ? 0 : getChartJson().hashCode());
        result = prime * result + ((getModelUsed() == null) ? 0 : getModelUsed().hashCode());
        result = prime * result + ((getPromptUsed() == null) ? 0 : getPromptUsed().hashCode());
        result = prime * result + ((getAnalysis() == null) ? 0 : getAnalysis().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileId=").append(fileId);
        sb.append(", chartJson=").append(chartJson);
        sb.append(", modelUsed=").append(modelUsed);
        sb.append(", promptUsed=").append(promptUsed);
        sb.append(", analysis=").append(analysis);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
package com.sue.sueactiviti7.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * @author sue
 * @date 2020/9/14 14:36
 */

@Mapper
@Component
public interface ActivitiMapper {
    @Insert("<script> insert into formdata (PROC_DEF_ID_,PROC_INST_ID_,FORM_KEY_,Control_ID_,Control_VALUE_)"+
            " values" +
            "<foreach collection=\"maps\" item=\"formData\" index=\"index\" separator=\",\">"+
            "(#{formData.PROC_DEF_ID_,jdbcType=VARCHAR},"+
            "#{formData.PROC_INST_ID_,jdbcType=VARCHAR},"+
            "#{formData.FORM_KEY_,jdbcType=VARCHAR},"+
            "#{formData.Control_ID_,jdbcType=VARCHAR},"+
            "#{formData.Control_VALUE_,jdbcType=VARCHAR})"+
            "</foreach></script>")
    int insertFormData(@Param("maps")List<HashMap<String,Object>> maps);



    //读取表单
    @Select("select Control_ID_,Control_VALUE_ from formData where PROC_INST_ID = #{PROC_INST_ID}")
    List<HashMap<String,Object>> selectFormData(@Param("PROC_INST_ID")String PROC_INST_ID);


    //获取用户列表
    //Select name,username from user
    @Select("Select name,username from user")
    List<HashMap<String,Object>> selectUser();


}

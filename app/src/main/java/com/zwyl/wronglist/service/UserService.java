package com.zwyl.wronglist.service;

import com.zwyl.wronglist.dialog.bean.BeanAllYear;
import com.zwyl.wronglist.dialog.bean.BeanNowYear;
import com.zwyl.wronglist.http.HttpResult;
import com.zwyl.wronglist.main.BeanHomeGrid;
import com.zwyl.wronglist.main.BeanTextBookGrid;
import com.zwyl.wronglist.main.detaile.BeanNote;
import com.zwyl.wronglist.main.detaile.ResultInfoBean;
import com.zwyl.wronglist.main.detaile.UpdateBean;
import com.zwyl.wronglist.main.subject.BeanSubject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {
    @FormUrlEncoded
    @POST("user/login")
    Observable<HttpResult<String>> login(@Field("password") String password, @Field("username") String username);

    /**
     * 1.查询当前学年接口：
     * 调用接口：/ys-manager/guidebook/currentYearByStudentId
     * 接口说明：首页显示该学生的当前学年
     * 请求方式：post
     * <p>
     * 参数名称	参数命名	说明
     * 用户token	用户登录时获得的token	用户登录后，后端返回到app的token凭证，是个uuid
     * <p>
     * <p>
     * 返回数据列表：
     * 名称	字段命名	说明
     * 学年id	academicYearId
     * 学年名称	academicYear
     */
    @FormUrlEncoded
    @POST("guidebook/currentYearByStudentId")
    Observable<HttpResult<BeanNowYear>> currentYearByStudentId(@Field("nullStr") String nullStr);

    /**
     * 2.查询该学生所有学年接口：
     * 调用接口：/ys-manager/guidebook/allYearsByStudentId
     * 接口说明：点击该学生的当前学年查询所有历史学年接口
     * 请求方式：post
     * <p>
     * 参数名称	参数命名	说明
     * 用户token	用户登录时获得的token	用户登录后，后端返回到app的token凭证，是个uuid
     * <p>
     * <p>
     * 返回数据列表：
     * 名称	字段命名	说明
     * 学年id	academicYearId
     * 学年名称	academicYear
     **/
    @FormUrlEncoded
    @POST("guidebook/allYearsByStudentId")
    Observable<HttpResult<List<BeanAllYear>>> allYearsByStudentId(@Field("nullStr") String nullStr);

    /**
     * 3.根据学年查该学生所在年级的所有科目
     * 调用接口：/guidebook/selectGradeSubejectList
     * 接口说明：选择学年显示学年下的所有科目
     * 刚进入页面默认返回当前学年的科目，选择学年后返回该学年下的科目
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 学年id	academicYearId
     * 用户token	Token
     * 当前页	pageNo
     * 每页显示条数	PageSize	默认一页10条数据
     * <p>
     * <p>
     * 返回数据列表：
     * 名称	字段命名	说明
     * 科目id	schoolSubjectId
     * 科目名称	schoolSubjectName
     **/
    @FormUrlEncoded
    @POST("guidebook/selectGradeSubejectList")
    Observable<HttpResult<List<BeanHomeGrid>>> selectGradeSubejectList(@Field("academicYearId") String password);

    /**
     * 4.根据学年和科目查该学生课本详情
     * 调用接口：/guidebook/selectTextBook
     * 接口说明：显示科目下的课本接口
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 学年id	academicYearId
     * 用户token	Token
     * 科目id	schoolSubjectId
     **/
    @FormUrlEncoded
    @POST("guidebook/selectTextBook")
    Observable<HttpResult<List<BeanTextBookGrid>>> selectTextBook(@Field("academicYearId") String academicYearId, @Field("schoolSubjectId") String schoolSubjectId);

    /**
     * 5.课本树结构接口
     * 调用接口：/guidebook/selectTextBookChapter
     * 接口说明：显示课本章节结构
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 课本id	textBookId
     * 课本章节父id	textBookChapterParentId	用户点击哪个章节，就把该章节id当成章节父id传给后台，查出它下面的子章节
     **/
    @FormUrlEncoded
    @POST("guidebook/selectTextBookChapter")
    Observable<HttpResult<List<ResultInfoBean>>> selectTextBookChapter(@Field("textBookId") String textBookId);

    /**
     *点击某课本，查课本下的所有错题集接口
     调用接口：/exercisesCollection/selectExercisesCollectionByTextBookId
     接口说明：
     请求方式：post

     请求参数：
     名称	字段命名	说明
     课本id	textBookId
     学生	Token
     排序	orderBy	升序:asc
     降序:desc
     不传参数，默认按照时间升序排列
     **/
    @FormUrlEncoded
    @POST("exercisesCollection/selectExercisesCollectionByTextBookId")
    Observable<HttpResult<List<BeanSubject>>> selectExercisesCollectionByTextBookId(@Field("textBookId") String textBookId, @Field("orderBy") String orderBy);

    /**
     * 点击某课本，查课本下的所有错题集接口
     调用接口：/exercisesCollection/selectExercisesCollectionByChapterId
     接口说明：
     请求方式：post

     请求参数：
     名称	字段命名	说明
     课本id	textBookChapterId
     学生	Token
     排序	orderBy	升序:asc
     降序:desc
     不传参数，默认按照时间升序排列
     **/
//    @FormUrlEncoded
//    @POST("workSupport/selectWorkSupportByChapterId")
//    Observable<HttpResult<List<BeanDetaile>>> selectExercisesCollectionByChapterId(@Field("textBookChapterId") String textBookId, @Field("orderBy") String orderBy);

    @FormUrlEncoded
    @POST("exercisesCollection/selectExercisesCollectionByChapterId")
    Observable<HttpResult<List<BeanSubject>>> selectExercisesCollectionByChapterId(@Field("textBookChapterId") String textBookId, @Field("orderBy") String orderBy);
    /**
     * 新增导学笔记接口：
     * 调用接口：/guidebook/insertGuideNoteBook
     * 接口说明：导学笔记新增
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 导学本id	guideSourceId
     * 学生	Token
     * 笔记内容	noteContent
     **/
    @FormUrlEncoded
    @POST("guidebook/insertGuideNoteBook")
    Observable<HttpResult<String>> insertGuideNoteBook(@Field("guideSourceId") String guideSourceId, @Field("noteContent") String noteContent);

    /**
     * 查导学笔记接口：
     * 调用接口：/guidebook/selectSourceNoteBook
     * 接口说明：查所有导学笔记
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 导学本id	guideSourceId
     * 学生	Token
     * <p>
     * 返回数据：返回list。返回多个参数，只用下面三个
     * 名称	字段命名	说明
     * 笔记id	noteId
     * 笔记内容	noteContent
     * 创建时间	createTime
     **/
    @FormUrlEncoded
    @POST("guidebook/selectSourceNoteBook")
    Observable<HttpResult<List<BeanNote>>> selectSourceNoteBook(@Field("guideSourceId") String guideSourceId);

    /**
     * 8.学生下载导学本，给后台传参，后台记录下载状态
     * 调用接口：/guidebook/downloadGuideBook
     * 接口说明：
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 导学本id	guideSoureId
     * 课本章节父id	token
     **/
    @FormUrlEncoded
    @POST("guidebook/downloadGuideBook")
    Observable<HttpResult<String>> downloadGuideBook(@Field("guideSourceId") String guideSourceId);

    /**
     * 查导学测试题接口：
     * 调用接口：/guidebook/selectGuideExercises
     * 接口说明：查导学本下面的测试题
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 导学本id	guideSourceId
     * cmStudentId
     **/
    @FormUrlEncoded
    @POST("workSupport/selectWorkSupportInfo")
    Observable<HttpResult<List<BeanSubject>>> selectWorkSupportInfo(@Field("workSupportId") String workSupportId);

    /**
     * 学生提交作业接口：
     * 调用接口：/guidebook/submitWork
     * 接口说明：学生提交答案，题库作业，附件作业
     * 请求方式：post
     * <p>
     * 请求参数：
     * 名称	字段命名	说明
     * 学生	Token
     * 作业id，存作业id和导学测试的导学本id	workId
     * 作业记录id	workPushLogId
     * 作业类型	workType
     * 题目详情	info
     * 题目id	exerciseId
     * 答案	studentAnswer
     **/
    @Multipart
    @POST("homework/submitWork")
    Observable<HttpResult<String>> submitWork(@Part List<MultipartBody.Part> file);

    /**
     * 加入错题集接口。。网络作业，作业辅导等app加入错题集都用该接口
     * 调用接口：/exercisesCollection/addMistakesCollection
     * 接口说明：加入错题集通用接口，别的作业辅导，我的作业公用接口
     * 请求方式：post
     **/
    @FormUrlEncoded
    @POST("exercisesCollection/addMistakesCollection")
    Observable<HttpResult<String>> addMistakesCollection(@FieldMap Map<String, String> map);

    /**
     * 移出错题集接口.
     * 调用接口：/exercisesCollection/deleteMistakesCollection
     * 接口说明：作业辅导，我的作业公用接口
     * 请求方式：post
     **/
    @FormUrlEncoded
    @POST("exercisesCollection/deleteMistakesCollection")
    Observable<HttpResult<String>> deleteMistakesCollection(@Field("workId") String workId,@Field("exerciseId") String exerciseId);
    /**
     * 接口名：/app/guidebook/selectAppUpdate
     * <p>
     * 参数：cmStudentId,appId(应用id)
     * <p>
     * 返回参数：
     * appVersionId:版本号id
     * appVersionName：版本名称
     * fileUrl：文件url
     */
    @FormUrlEncoded
    @POST("guidebook/selectAppUpdate")
    Observable<HttpResult<UpdateBean>> selectAppUpdate(@Field("appId") String appId);

}
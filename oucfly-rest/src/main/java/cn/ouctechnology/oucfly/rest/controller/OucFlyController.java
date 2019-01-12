package cn.ouctechnology.oucfly.rest.controller;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.operator.Operator;
import cn.ouctechnology.oucfly.operator.XnXq;
import cn.ouctechnology.oucfly.operator.coin.Coin;
import cn.ouctechnology.oucfly.operator.coin.CoinClass;
import cn.ouctechnology.oucfly.operator.coin.CoinClassEntity;
import cn.ouctechnology.oucfly.operator.coin.CoinEntity;
import cn.ouctechnology.oucfly.operator.dept.Dept;
import cn.ouctechnology.oucfly.operator.dept.DeptFilter;
import cn.ouctechnology.oucfly.operator.exam.Exam;
import cn.ouctechnology.oucfly.operator.grade.GradeDetail;
import cn.ouctechnology.oucfly.operator.grade.GradeScore;
import cn.ouctechnology.oucfly.operator.grade.GradeScoreEntity;
import cn.ouctechnology.oucfly.operator.order.ClassOrder;
import cn.ouctechnology.oucfly.operator.order.MajorOrder;
import cn.ouctechnology.oucfly.operator.order.OrderEntity;
import cn.ouctechnology.oucfly.operator.query.Query;
import cn.ouctechnology.oucfly.operator.student.*;
import cn.ouctechnology.oucfly.operator.table.ClassTable;
import cn.ouctechnology.oucfly.rest.interceptor.OucFlyAttribute;
import cn.ouctechnology.oucfly.rest.exception.OucFlyRestException;
import cn.ouctechnology.oucfly.rest.service.OucFlyService;
import cn.ouctechnology.oucfly.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: oucfly-rest
 * @author: ZQX
 * @create: 2018-12-22 10:55
 * @description: TODO
 **/
@RestController
@Validated
@SuppressWarnings("unchecked")
@Api(value = "OucFlyController", description = "OucFlyRest接口大全")
@RequestMapping(method = RequestMethod.POST)
public class OucFlyController {

    @Autowired
    private OucFlyService oucFlyService;

    @ApiOperation(value = "登录", notes = "登录教务处，登录成功返回一个token，" +
            "下面的每次请求中均需要把这个token以键值对的形式放在cookie中，" +
            "每个token有效时间默认30分钟，失败需要重新获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "教务处密码", required = true, dataType = "String")
    })
    @RequestMapping("/login")
    public Result login(@NotNull String username, @NotNull String password) {
        return oucFlyService.login(username, password);
    }

    @ApiOperation(value = "成绩详情", notes = "获取成绩详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    required = true, dataType = "Integer")
    })
    @RequestMapping("/grade/detail")
    public Result gradeDetail(@OucFlyAttribute OucFly oucFly, @NotNull String userCode,
                              Integer xn, @Max(2) @Min(0) Integer xq) {
        Operator operator;
        if (xn == null || xq == null) operator = new GradeDetail(userCode);
        else operator = new GradeDetail(userCode, new XnXq(xn, XnXq.Xq.values()[xq]));
        return oucFly.run(operator);
    }

    @ApiOperation(value = "加权平均分", notes = "获取加权平均分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "学年，2015、2016....一学年三个学期",
                    dataType = "Integer"),
    })
    @RequestMapping("/grade/score")
    public Result gradeScore(@OucFlyAttribute OucFly oucFly, @NotNull String userCode,
                             Integer xn, @Max(2) @Min(0) Integer xq, Integer year) {
        Operator operator;
        if (year != null) operator = new GradeScore(userCode, year);
        else if (xn == null || xq == null) operator = new GradeScore(userCode);
        else operator = new GradeScore(userCode, new XnXq(xn, XnXq.Xq.values()[xq]));
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取课表", notes = "获取课表，返回一个三维数组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    required = true, dataType = "Integer")
    })
    @RequestMapping("/table")
    public Result table(@OucFlyAttribute OucFly oucFly, @NotNull String userCode,
                        @NotNull Integer xn, @NotNull @Max(2) @Min(0) Integer xq) {
        Operator operator = new ClassTable(userCode, new XnXq(xn, XnXq.Xq.values()[xq]));
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取同学选课币", notes = "获取每个同学某门课的选课币")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "classCode", value = "课程号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    required = true, dataType = "Integer")
    })
    @RequestMapping("/coin")
    public Result coin(@OucFlyAttribute OucFly oucFly, @NotNull String userCode,
                       @NotNull String classCode, @NotNull Integer xn,
                       @NotNull @Max(2) @Min(0) Integer xq) {
        Operator operator = new Coin(userCode, classCode, new XnXq(xn, XnXq.Xq.values()[xq]));
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取课程选课币", notes = "获取每门课所有同学投入的选课币")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "classCode", value = "课程号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    required = true, dataType = "Integer")
    })
    @RequestMapping("/coin/class")
    public Result coinClass(@OucFlyAttribute OucFly oucFly, @NotNull String classCode,
                            @NotNull Integer xn, @NotNull @Max(2) @Min(0) Integer xq) {
        CoinClass operator = new CoinClass(classCode, new XnXq(xn, XnXq.Xq.values()[xq]));
        Result<CoinClassEntity> res = oucFly.run(operator);
        if (res.isSuccess()) {
            CoinClassEntity content = res.getContent();
            List<CoinEntity> coins = content.getData();
            coins.sort((o1, o2) -> o2.getCoin() - o1.getCoin());
        }
        return res;
    }

    @ApiOperation(value = "获取院系列表", notes = "获取院系列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "deptName", value = "学院名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deptCode", value = "学院代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "专业名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorCode", value = "专业代码", required = true, dataType = "String")
    })
    @RequestMapping("/dept")
    public Result dept(@OucFlyAttribute OucFly oucFly, @NotNull Integer grade,
                       String deptName, String deptCode, String majorName, String majorCode) {
        DeptFilter deptFilter = new DeptFilter(grade);
        if (deptCode != null) deptFilter.setDept(deptCode);
        else if (deptName != null) deptFilter.filterDept(deptName);
        if (majorCode != null) deptFilter.setMajor(majorCode);
        else if (majorName != null) deptFilter.filterMajor(majorName);
        Operator operator = new Dept(deptFilter);
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取考试安排", notes = "获取考试安排")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "学号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "classCode", value = "课程号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "考试类型，1:开学初补缓考，2:期中考试，3:期末考试",
                    required = true, dataType = "Integer")
    })
    @RequestMapping("/exam")
    public Result exam(@OucFlyAttribute OucFly oucFly, @NotNull String userCode,
                       @NotNull Integer xn, @NotNull @Max(2) @Min(0) Integer xq,
                       @NotNull @Max(2) @Min(0) Integer type) {
        Operator operator = new Exam(userCode, new XnXq(xn, XnXq.Xq.values()[xq]), Exam.ExamType.values()[type]);
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取学生列表", notes = "获取某个院系或者选某门课的学生列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "deptName", value = "学院名称", dataType = "String"),
            @ApiImplicitParam(name = "deptCode", value = "学院代码", dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "专业名称", dataType = "String"),
            @ApiImplicitParam(name = "majorCode", value = "专业代码", dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "classCode", value = "课程号", dataType = "String")
    })
    @RequestMapping("/student/list")
    public Result studentList(@OucFlyAttribute OucFly oucFly, Integer grade, String deptName,
                              String deptCode, String majorName, String majorCode,
                              Integer xn, @Max(2) @Min(0) Integer xq, String classCode) {
        StudentFilter studentFilter = getStudentFilter(grade, deptName, deptCode, majorName, majorCode, xn, xq, classCode);
        Operator operator = new StudentList(studentFilter);
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取学生学号", notes = "获取某个院系或者选某门课的学生学号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "deptName", value = "学院名称", dataType = "String"),
            @ApiImplicitParam(name = "deptCode", value = "学院代码", dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "专业名称", dataType = "String"),
            @ApiImplicitParam(name = "majorCode", value = "专业代码", dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "classCode", value = "课程号", dataType = "String")
    })
    @RequestMapping("/student/code")
    public Result studentCode(@OucFlyAttribute OucFly oucFly, Integer grade, String deptName,
                              String deptCode, String majorName, String majorCode,
                              Integer xn, @Max(2) @Min(0) Integer xq, String classCode) {
        StudentFilter studentFilter = getStudentFilter(grade, deptName, deptCode, majorName, majorCode, xn, xq, classCode);
        Operator operator = new StudentCode(studentFilter);
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取学生信息", notes = "获取学生信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "姓名", dataType = "String"),
            @ApiImplicitParam(name = "userCode", value = "学号", dataType = "String"),
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "deptName", value = "学院名称", dataType = "String"),
            @ApiImplicitParam(name = "deptCode", value = "学院代码", dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "专业名称", dataType = "String"),
            @ApiImplicitParam(name = "majorCode", value = "专业代码", dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "classCode", value = "课程号", dataType = "String")
    })
    @RequestMapping("/student")
    public Result student(@OucFlyAttribute OucFly oucFly, String userName, String userCode,
                          Integer grade, String deptName, String deptCode, String majorName,
                          String majorCode, Integer xn, @Max(2) @Min(0) Integer xq,
                          String classCode) {
        StudentFilter studentFilter = null;
        Student operator = new Student();
        if (grade != null) {
            StudentDeptFilter deptFilter = new StudentDeptFilter(grade);
            if (deptCode != null) deptFilter.setDept(deptCode);
            else if (deptName != null) deptFilter.filterDept(deptName);
            if (majorCode != null) deptFilter.setMajor(majorCode);
            else if (majorName != null) deptFilter.filterMajor(majorName);
            studentFilter = deptFilter;
        } else if (xn != null && xq != null && classCode != null) {
            studentFilter = new StudentClassFilter(new XnXq(xn, XnXq.Xq.values()[xq]), classCode);
        }
        if (studentFilter != null) operator.setFilter(studentFilter);
        if (userCode != null) operator.setUserCode(userCode);
        if (userName != null) operator.setUserName(userName);
        return oucFly.run(operator);
    }

    @ApiOperation(value = "获取班级排名", notes = "获取班级排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "classCode", value = "课程号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    required = true, dataType = "Integer")
    })
    @RequestMapping("/order/class")
    public Result orderClass(@OucFlyAttribute OucFly oucFly, @NotNull String classCode,
                             @NotNull Integer xn, @NotNull @Max(2) @Min(0) Integer xq) {
        ClassOrder operator = new ClassOrder(new XnXq(xn, XnXq.Xq.values()[xq]), classCode);
        Result<OrderEntity> result = oucFly.run(operator);
        OrderEntity content = result.getContent();
        List<GradeScoreEntity> data = content.getData();
        data.sort(((o1, o2) -> (int) (o2.getGrade() - o1.getGrade())));
        return result;
    }

    @ApiOperation(value = "获取班级排名", notes = "获取班级排名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "年级", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "deptName", value = "学院名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "deptCode", value = "学院代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorName", value = "专业名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "majorCode", value = "专业代码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "xn", value = "学年", dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "year", value = "学年，2015、2016....一学年三个学期",
                    dataType = "Integer"),
    })
    @RequestMapping("/order/major")
    public Result orderMajor(@OucFlyAttribute OucFly oucFly, @NotNull Integer grade,
                             String deptName, String deptCode, String majorName, String majorCode,
                             Integer year, Integer xn, @Max(2) @Min(0) Integer xq) {
        StudentDeptFilter deptFilter = new StudentDeptFilter(grade);
        if (deptCode != null) deptFilter.setDept(deptCode);
        else if (deptName != null) deptFilter.filterDept(deptName);
        if (majorCode != null) deptFilter.setMajor(majorCode);
        else if (majorName != null) deptFilter.filterMajor(majorName);
        MajorOrder operator;
        if (year != null) operator = new MajorOrder(deptFilter, year);
        else if (xn == null || xq == null) operator = new MajorOrder(deptFilter);
        else operator = new MajorOrder(deptFilter, new XnXq(xn, XnXq.Xq.values()[xq]));
        Result<OrderEntity> result = oucFly.run(operator);
        OrderEntity content = result.getContent();
        List<GradeScoreEntity> data = content.getData();
        data.sort(((o1, o2) -> (int) (o2.getGrade() - o1.getGrade())));
        return result;
    }

    @ApiOperation(value = "查询课程信息", notes = "查询课程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xn", value = "学年", dataType = "Integer"),
            @ApiImplicitParam(name = "xq", value = "学期，0:夏季学期，1:秋季学期，2:春季学期",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "课程类别，0:公共基础课，1:专业课，2:通识课",
                    dataType = "Integer"),
            @ApiImplicitParam(name = "className", value = "课程名称", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Integer"),

    })
    @RequestMapping("/query")
    public Result query(@OucFlyAttribute OucFly oucFly, @NotNull Integer xn,
                        @NotNull @Max(2) @Min(0) Integer xq, @NotNull String className,
                        @NotNull @Max(2) @Min(0) Integer type, Integer page) {
        Query operator = new Query(new XnXq(xn, XnXq.Xq.values()[xq]),
                Query.ClassType.values()[type], className);
        if (page != null) operator.setCurrentPage(page);
        return oucFly.run(operator);
    }


    private StudentFilter getStudentFilter(Integer grade, String deptName, String deptCode, String majorName, String majorCode, Integer xn, @Max(2) @Min(0) Integer xq, String classCode) {
        StudentFilter studentFilter;
        if (grade != null) {
            StudentDeptFilter deptFilter = new StudentDeptFilter(grade);
            if (deptCode != null) deptFilter.setDept(deptCode);
            else if (deptName != null) deptFilter.filterDept(deptName);
            if (majorCode != null) deptFilter.setMajor(majorCode);
            else if (majorName != null) deptFilter.filterMajor(majorName);
            studentFilter = deptFilter;
        } else {
            if (xn == null || xq == null || classCode == null)
                throw new OucFlyRestException(
                        "必须选择年级院系或者某个学期的课程才能获取学生列表");
            studentFilter = new StudentClassFilter(new XnXq(xn, XnXq.Xq.values()[xq]), classCode);
        }
        return studentFilter;
    }

    @RequestMapping("/check")
    public Result check(@NotNull String token) {
        return oucFlyService.check(token);
    }
}

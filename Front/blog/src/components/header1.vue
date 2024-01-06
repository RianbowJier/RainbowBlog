<!-- 头部公用 -->
<template>
    <div>
        <div class="header">
            <el-row>
                <el-col :span="8">
                    <div class="grid-content bg-purple"></div>
                </el-col>
                <el-col :span="8">
                    <div class="grid-content bg-purple-light"></div>
                </el-col>
                <el-col :span="8">
                    <div class="grid-content bg-purple"></div>
                </el-col>
            </el-row>
        </div>


        <div class="headImgBox" :style="{ backgroundImage: 'url(static/img/headbg05.jpg)' }">

            <div class="h-information">
                <img :src="this.$store.state.themeObj.head_portrait ? this.$store.state.themeObj.head_portrait : 'static/img/tou.png'"
                    alt="">

                <h2 class="h-description">
                    {{ this.$store.state.themeObj.autograph ? this.$store.state.themeObj.autograph :
                        "我曾踏足山巅，也曾进入低谷，二者都让我受益良多" }}
                </h2>
            </div>
        </div>

    </div>
</template>
<script>
import { logout } from '../api/user'
import { removeToken } from '../utils/auth'
import { getCategoryList } from '../api/category'
import {
    Typeit
} from '../utils/plug.js'
export default {
    data() { //选项 / 数据
        return {
            userInfo: '', //用户信息
            haslogin: false, //是否已登录
            classListObj: '', //分类
            activeIndex: '/', //当前选择的路由模块
            state: '', //icon点击状态
            pMenu: true, //手机端菜单打开
            // path:'',//当前打开页面的路径
            input: '', //input输入内容
            headBg: 'url(static/img/headbg05.jpg)', //头部背景图
            headTou: '', //头像
            projectList: '' //项目列表
        }
    },
    watch: {

    },
    methods: { //事件处理器
        handleOpen(key, keyPath) { //分组菜单打开
            // console.log(key, keyPath);
        },
        handleClose(key, keyPath) { //分组菜单关闭
            // console.log(key, keyPath);
        },
        searchChangeFun(e) { //input change 事件
            // console.log(e)
            if (this.input == '') {
                this.$store.state.keywords = '';
                this.$router.push({ path: '/' });
            }
        },
        getCategoryList() {
            getCategoryList().then((response) => {
                this.classListObj = response
            })
        },
        handleSelect(key, keyPath) { //pc菜单选择
            //    console.log(key, keyPath);
        },
        logoinFun: function (msg) { //用户登录和注册跳转
            // console.log(msg);
            localStorage.setItem('logUrl', this.$route.fullPath);
            // console.log(666,this.$router.currentRoute.fullPath);
            if (msg == 0) {
                this.$router.push({
                    path: '/Login?login=0'
                });
            } else {
                this.$router.push({
                    path: '/Login?login=1'
                });
            }
        },
        // 用户退出登录
        userlogout: function () {
            var that = this;
            this.$confirm('是否确认退出?', '退出提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                // console.log(that.$route.path);

                logout().then((response) => {
                    removeToken()
                    localStorage.removeItem('userInfo');
                    that.haslogin = false;
                    window.location.reload();
                    that.$message({
                        type: 'success',
                        message: '退出成功!'
                    });
                    if (that.$route.path == '/UserInfo') {
                        that.$router.push({
                            path: '/'
                        });
                    }
                })
            }).catch(() => {
                //
            });

        },
        routeChange: function () {
            var that = this;
            that.pMenu = true
            this.activeIndex = this.$route.path == '/' ? '/Home' : this.$route.path;
            if (localStorage.getItem('userInfo')) { //存储用户信息
                that.haslogin = true;
                that.userInfo = JSON.parse(localStorage.getItem('userInfo'));
                // console.log(that.userInfo);
            } else {
                that.haslogin = false;
            }
            //获取分类
            this.getCategoryList()

            if ((this.$route.name == "Share" || this.$route.name == "Home") && this.$store.state.keywords) {
                this.input = this.$store.state.keywords;
            } else {
                this.input = '';
                this.$store.state.keywords = '';
            }
        }
    },
    components: { //定义组件

    },
    watch: {
        // 如果路由有变化，会再次执行该方法
        '$route': 'routeChange'
    },
    created() { //生命周期函数
        //判断当前页面是否被隐藏
        var that = this;
        var hiddenProperty = 'hidden' in document ? 'hidden' :
            'webkitHidden' in document ? 'webkitHidden' :
                'mozHidden' in document ? 'mozHidden' :
                    null;
        var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
        var onVisibilityChange = function () {
            if (!document[hiddenProperty]) { //被隐藏
                if (that.$route.path != '/DetailShare') {
                    if (localStorage.getItem('userInfo')) {
                        that.haslogin = true;
                    } else {
                        that.haslogin = false;
                    }
                }
            }
        }
        document.addEventListener(visibilityChangeEvent, onVisibilityChange);
        // console.log();
        this.routeChange();


    },
    mounted() { //页面元素加载完成
        var that = this;
        var timer = setTimeout(function () {
            Typeit(that.$store.state.themeObj.user_start, "#luke"); //打字机效果
            clearTimeout(timer);
        }, 500);
    }
}
</script>

<style>/**导航栏 */</style>
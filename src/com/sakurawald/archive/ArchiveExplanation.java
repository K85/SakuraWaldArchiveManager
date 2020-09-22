package com.sakurawald.archive;

import com.sakurawald.debug.LoggerManager;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArchiveExplanation {

    /**
     * 判断是否启用regex的标志文本
     */
    private static final String USE_REGEX_SIGN = "$REGEX";
    /**
     * 空的ArchiveExplanation
     */
    private static final ArchiveExplanation NULL_ARCHIVE_EXPLANATION = new ArchiveExplanation("无", "NONE", "NONE");
    private String explanation = null;
    private String sign = null;
    private String fileName = null;
    private transient Pattern regexPattern = null;

    public ArchiveExplanation(String explanation, String sign, String fileName) {
        this.explanation = explanation;
        this.sign = sign;
        this.fileName = fileName;
    }

    /**
     * @return 获取代表null的ArchiveExplanation
     */
    public static ArchiveExplanation getNullArchiveExplanation() {
        return NULL_ARCHIVE_EXPLANATION;
    }

    /**
     * @return 由fileName转义后得到的Regex.
     */
    public static String translateRegex(String fileName) {
        return fileName.replace(USE_REGEX_SIGN, "");
    }

    /**
     * @return 默认配置好的一整套ArchiveExplanation.
     */
    public static ArrayList<ArchiveExplanation> getDefaultArchiveExplanations() {

        ArrayList<ArchiveExplanation> result = new ArrayList<ArchiveExplanation>();

        /**
         * 用户数据
         */
        result.add(new ArchiveExplanation("用户列表", "USERS", "users.dat"));
        result.add(new ArchiveExplanation("单个用户数据", "SINGLE_USER", "$REGEXuser\\d+?\\.dat"));

        /**
         * 冒险模式
         */
        result.add(new ArchiveExplanation("冒险模式", "ADVENTURE", "game1_0.dat"));

        /**
         * 生存模式
         */
        //有尽模式
        result.add(new ArchiveExplanation("生存模式_白天", "SURVIVAL_DAY", "game1_1.dat"));
        result.add(new ArchiveExplanation("生存模式_黑夜", "SURVIVAL_NIGHT", "game1_2.dat"));
        result.add(new ArchiveExplanation("生存模式_浓雾", "SURVIVAL_FOG", "game1_4.dat"));
        result.add(new ArchiveExplanation("生存模式_屋顶", "SURVIVAL_ROOF", "game1_5.dat"));
        result.add(new ArchiveExplanation("生存模式_白天_困难", "SURVIVAL_DAY_HARD", "game1_6.dat"));
        result.add(new ArchiveExplanation("生存模式_黑夜_困难", "SURVIVAL_NIGHT_HARD", "game1_7.dat"));
        result.add(new ArchiveExplanation("生存模式_泳池_困难", "SURVIVAL_POOL_HARD", "game1_8.dat"));
        result.add(new ArchiveExplanation("生存模式_浓雾_困难", "SURVIVAL_FOG_HARD", "game1_9.dat"));
        result.add(new ArchiveExplanation("生存模式_屋顶_困难", "SURVIVAL_ROOF_HARD", "game1_10.dat"));
        // 无尽模式
        result.add(new ArchiveExplanation("生存模式_白天：(无尽版)", "SURVIVAL_DAY_ENDLESS", "game1_11.dat"));
        result.add(new ArchiveExplanation("生存模式_黑夜：(无尽版)", "SURVIVAL_NIGHT_ENDLESS", "game1_12.dat"));
        result.add(new ArchiveExplanation("生存模式_浓雾：(无尽版)", "SURVIVAL_FOG_ENDLESS", "game1_14.dat"));
        result.add(new ArchiveExplanation("生存模式_泳池：(无尽版)", "SURVIVAL_POOL_ENDLESS", "game1_13.dat"));
        result.add(new ArchiveExplanation("生存模式_屋顶：(无尽版)", "SURVIVAL_ROOF_ENDLESS", "game1_15.dat"));

        /**
         * 迷你游戏模式
         */

        result.add(new ArchiveExplanation("迷你游戏_坚果保龄球", "MINI_GAMES_WALL_NUT_BOWLING", "game1_17.dat"));
        result.add(new ArchiveExplanation("迷你游戏_僵尸快跑", "MINI_GAMES_ZOMBIE_NIMBLE_ZOMBIE_QUICK", "game1_29.dat"));
        result.add(new ArchiveExplanation("迷你游戏_小僵尸大麻烦", "MINI_GAMES_BIG_TROUBLE_LITTLE_ZOMBIE", "game1_24.dat"));
        result.add(new ArchiveExplanation("迷你游戏_植物僵尸", "MINI_GAMES_ZOM_BOTANY", "game1_16.dat"));
        result.add(new ArchiveExplanation("迷你游戏_老虎机", "MINI_GAMES_SLOT_MACHINE", "game1_18.dat"));
        result.add(new ArchiveExplanation("迷你游戏_雨中种植物", "MINI_GAMES_ITS_RAINING_SEEDS", "game1_19.dat"));
        result.add(new ArchiveExplanation("迷你游戏_宝石迷阵", "MINI_GAMES_BEGHOULED", "game1_20.dat"));
        result.add(new ArchiveExplanation("迷你游戏_隐形食脑者", "MINI_GAMES_INVISI_GHOUL", "game1_21.dat"));
        result.add(new ArchiveExplanation("迷你游戏_看星星", "MINI_GAMES_SEEING_STARS", "game1_22.dat"));
        result.add(new ArchiveExplanation("迷你游戏_僵尸水族馆", "MINI_GAMES_ZOMBIQUARIUM", "game1_23.dat"));
        result.add(new ArchiveExplanation("迷你游戏_宝石迷阵2", "MINI_GAMES_BEGHOULED_TWIST", "game1_24.dat"));
//        result.add(new ArchiveExplanation("迷你游戏_传送门", "MINI_GAMES_PORTAL_COMBAT","game1_25.dat"));
        result.add(new ArchiveExplanation("迷你游戏_保护传送门", "MINI_GAMES_PORTAL_COMBAT", "game1_26.dat"));
        result.add(new ArchiveExplanation("迷你游戏_你看，他们像柱子一样", "MINI_GAMES_COLUMN_LIKE_YOU_SEE_EM", "game1_27.dat"));
        result.add(new ArchiveExplanation("迷你游戏_雪橇区", "MINI_GAMES_BOBSLED_BONANZA", "game1_28.dat"));
        result.add(new ArchiveExplanation("迷你游戏_锤僵尸", "MINI_GAMES_WHACK_A_ZOMBIE", "game1_30.dat"));
        result.add(new ArchiveExplanation("迷你游戏_植物僵尸2", "MINI_GAMES_ZOM_BOTANY_2", "game1_32.dat"));
        result.add(new ArchiveExplanation("迷你游戏_谁笑到最后", "MINI_GAMES_LAST_STAND", "game1_31.dat"));
        result.add(new ArchiveExplanation("迷你游戏_坚果保龄球2", "MINI_GAMES_WALL_NUT_BOWLING_2", "game1_33.dat"));
        result.add(new ArchiveExplanation("迷你游戏_跳跳舞会", "MINI_GAMES_POGO_PARTY", "game1_34.dat"));
        result.add(new ArchiveExplanation("迷你游戏_僵王博士的复仇", "MINI_GAMES_DR_ZOMBOSS_REVENGE", "game1_35.dat"));

        /**
         * 迷你游戏模式 >> 隐藏游戏
         */
        result.add(new ArchiveExplanation("迷你游戏_坚果保龄球艺术锦标赛", "ART_CHALLENGE_WALL_NUT", "game1_36.dat"));
        result.add(new ArchiveExplanation("迷你游戏_晴天", "MINI_GAMES_SUNNY_DAY", "game1_37.dat"));
        result.add(new ArchiveExplanation("迷你游戏_无草皮之地", "MINI_GAMES_UNSODDED", "game1_38.dat"));
        result.add(new ArchiveExplanation("迷你游戏_重要时间", "MINI_GAMES_BIG_TIME", "game1_39.dat"));
        result.add(new ArchiveExplanation("迷你游戏_挑战种太阳花的艺术", "MINI_GAMES_ART_CHALLENGE_SUNFLOWER", "game1_40.dat"));
        result.add(new ArchiveExplanation("迷你游戏_空袭", "MINI_GAMES_AIR_RAID", "game1_41.dat"));
        // 注意: 隐藏关_冰冻关卡 没有存档机制
        result.add(new ArchiveExplanation("迷你游戏_超乎寻常的压力", "MINI_GAMES_HIGH_GRAVITY ", "game1_44.dat"));
        result.add(new ArchiveExplanation("迷你游戏_坟墓模式", "MINI_GAMES_GRAVE_DANGER", "game1_45.dat"));
        result.add(new ArchiveExplanation("迷你游戏_你能把它挖出来吗？", "MINI_GAMES_CAN_U_DIG_IT?", "game1_46.dat"));
        result.add(new ArchiveExplanation("迷你游戏_黑暗的暴风雨夜", "MINI_GAMES_DARK_STORMY_NIGHT", "game1_47.dat"));
        result.add(new ArchiveExplanation("迷你游戏_蹦极闪电战", "MINI_GAMES_BUNGEE_BLITZ", "game1_48.dat"));
        result.add(new ArchiveExplanation("迷你游戏_Squirrel", "MINI_GAMES_SQUIRRE", "game1_49.dat"));

        /**
         * 解谜模式
         */
        result.add(new ArchiveExplanation("解谜模式_砸罐子_破罐者", "PUZZLE_VASEBREAKER_VASEBREAKER", "game1_51.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_全部留下", "PUZZLE_VASEBREAKER_TO_THE_LEFT", "game1_52.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_第三个罐子", "PUZZLE_VASEBREAKER_THIRD_VASE", "game1_53.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_连锁反应", "PUZZLE_VASEBREAKER_CHAIN_REACTION", "game1_54.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_M的意思是金属", "PUZZLE_VASEBREAKER_M_IS_FOR_METAL", "game1_55.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_胆怯的制陶工", "PUZZLE_VASEBREAKER_SCARY_POTTER", "game1_56.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_变戏法", "PUZZLE_VASEBREAKER_HOKEY_POKEY", "game1_57.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_另一个连锁反应", "PUZZLE_VASEBREAKER_ANOTHER_CHAIN_REACTION", "game1_58.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_王牌罐子", "PUZZLE_VASEBREAKER_ACE_OF_VASE", "game1_59.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_无尽", "PUZZLE_VASEBREAKER_VASEBREAKER_ENDLESS", "game1_60.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_我是僵尸", "PUZZLE_I_AM_A_ZOMBIE_ZOMBIE", "game1_61.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_我也是僵尸", "PUZZLE_I_AM_A_ZOMBIE_ZOMBIE_TOO", "game1_62.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_你能铲了它吗", "PUZZLE_I_AM_A_ZOMBIE_CAN_YOU_DIG_IT", "game1_63.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_完全傻了", "PUZZLE_I_AM_A_ZOMBIE_TOTALLY_NUTS", "game1_64.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_死亡飞艇", "PUZZLE_I_AM_A_ZOMBIE_DEAD_ZEPPELIN", "game1_65.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_我烂了", "PUZZLE_I_AM_A_ZOMBIE_ME_SMASH", "game1_66.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_僵尸摇摆", "PUZZLE_I_AM_A_ZOMBIE_ZOM_BOOGIE", "game1_67.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_三连击", "PUZZLE_I_AM_A_ZOMBIE_THREE_HIT_WONDER", "game1_68.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_你所有的大脑都是属于我的", "PUZZLE_I_AM_A_ZOMBIE_ALL_YOUR_BRAINZ_R_BELONG_TO_US", "game1_69.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_无尽", "PUZZLE_I_AM_A_ZOMBIE_ZOMBIE_ENDLESS", "game1_70.dat"));


        /**
         * 自定义备份文件
         */
        result.add(new ArchiveExplanation("你的自定义备份文件的注解", "YOUR_CUSTOM_FILE_SIGN", "your_custom_file_name.dat"));

        return result;
    }

    /**
     * 给传入的[ArchiveFile]分析ArchiveExplanation注解
     *
     * @reutrn 添加好相应注解的原ArchiveFile, 若没有符合的ArchiveExplanation, 则会添加一个NullArchiveExplanation.
     */
    public static void analyseArchiveExplanation(ArchiveFile archiveFile) {

        LoggerManager.logDebug("Rollback", "Start Analyse ArchiveExplanation >> archiveFile = " + archiveFile.getFile().getName());

        ArchiveBean ab = archiveFile.getOwner_ArchiveBean();

        // 遍历该ArchiveFile所属的GameVersion的所有ArchiveExplanation
        for (ArchiveExplanation ae : ab.getOwner_ArchiveSeries().getOwner_GameVersion().getArchieveExplanations()) {

            if (ae.isMatch(archiveFile) == true) {
                LoggerManager.logDebug("回档", "Analyse ArchiveExplanation Result: ArchiveFile '" + archiveFile.getFile().getName() + "' matches ArchiveExplanation '" + ae.getExplanation() + "'");

                // Set ArchiveExplanation
                archiveFile.setArchiveExplanation(ae);
            }

        }

        // 判断是否有符合条件的ArchiveExplnation, 此处防NPE.
        if (archiveFile.getArchiveExplanation() == null) {
            archiveFile.setArchiveExplanation(ArchiveExplanation.getNullArchiveExplanation());
        }

    }

    public static void analyseArchiveExplanation(ArrayList<ArchiveFile> archiveFiles) {

        for (ArchiveFile af : archiveFiles) {
            analyseArchiveExplanation(af);
        }

    }

    public String getSign() {
        return sign;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * 判断传入的ArchiveFile是否满足本ArchiveExplanation
     */
    public boolean isMatch(ArchiveFile archiveFile) {

        // 判断 "普通文本模式"
        if (this.isUseRegex() == false) {
            return archiveFile.getFile().getName().equalsIgnoreCase(this.getFileName());
        }

        // 判断 "正则表达式模式"
        Matcher matcher = getPattern().matcher(archiveFile.getFile().getName());
        return matcher.matches();
    }

    /**
     * 带缓存的Pattern
     */
    public Pattern getPattern() {

        if (this.regexPattern == null) {
            this.regexPattern = Pattern.compile(translateRegex(fileName));
        }

        return this.regexPattern;
    }

    /**
     * @return 该ArchiveExplanation的fileName是否用Regex来匹配.
     */
    public boolean isUseRegex() {
        return fileName.contains(USE_REGEX_SIGN);
    }

}

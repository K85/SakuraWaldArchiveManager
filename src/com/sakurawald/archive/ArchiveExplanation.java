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
        result.add(new ArchiveExplanation("单个用户数据", "SINGLE_USER", USE_REGEX_SIGN + "user\\d+?\\.dat"));

        /**
         * 冒险模式
         * "game1
         * USE_REGEX_SIGN + "game\\d+?
         */
        result.add(new ArchiveExplanation("冒险模式", "ADVENTURE", USE_REGEX_SIGN + "game\\d+?_0\\.dat"));

        /**
         * 生存模式
         */
        //有尽模式
        result.add(new ArchiveExplanation("生存模式_白天", "SURVIVAL_DAY", USE_REGEX_SIGN + "game\\d+?_1\\.dat"));
        result.add(new ArchiveExplanation("生存模式_黑夜", "SURVIVAL_NIGHT", USE_REGEX_SIGN + "game\\d+?_2\\.dat"));
        result.add(new ArchiveExplanation("生存模式_浓雾", "SURVIVAL_FOG", USE_REGEX_SIGN + "game\\d+?_4\\.dat"));
        result.add(new ArchiveExplanation("生存模式_屋顶", "SURVIVAL_ROOF", USE_REGEX_SIGN + "game\\d+?_5\\.dat"));
        result.add(new ArchiveExplanation("生存模式_白天_困难", "SURVIVAL_DAY_HARD", USE_REGEX_SIGN + "game\\d+?_6\\.dat"));
        result.add(new ArchiveExplanation("生存模式_黑夜_困难", "SURVIVAL_NIGHT_HARD", USE_REGEX_SIGN + "game\\d+?_7\\.dat"));
        result.add(new ArchiveExplanation("生存模式_泳池_困难", "SURVIVAL_POOL_HARD", USE_REGEX_SIGN + "game\\d+?_8\\.dat"));
        result.add(new ArchiveExplanation("生存模式_浓雾_困难", "SURVIVAL_FOG_HARD", USE_REGEX_SIGN + "game\\d+?_9\\.dat"));
        result.add(new ArchiveExplanation("生存模式_屋顶_困难", "SURVIVAL_ROOF_HARD", USE_REGEX_SIGN + "game\\d+?_10\\.dat"));
        // 无尽模式
        result.add(new ArchiveExplanation("生存模式_白天：(无尽版)", "SURVIVAL_DAY_ENDLESS", USE_REGEX_SIGN + "game\\d+?_11\\.dat"));
        result.add(new ArchiveExplanation("生存模式_黑夜：(无尽版)", "SURVIVAL_NIGHT_ENDLESS", USE_REGEX_SIGN + "game\\d+?_12\\.dat"));
        result.add(new ArchiveExplanation("生存模式_浓雾：(无尽版)", "SURVIVAL_FOG_ENDLESS", USE_REGEX_SIGN + "game\\d+?_14\\.dat"));
        result.add(new ArchiveExplanation("生存模式_泳池：(无尽版)", "SURVIVAL_POOL_ENDLESS", USE_REGEX_SIGN + "game\\d+?_13\\.dat"));
        result.add(new ArchiveExplanation("生存模式_屋顶：(无尽版)", "SURVIVAL_ROOF_ENDLESS", USE_REGEX_SIGN + "game\\d+?_15\\.dat"));

        /**
         * 迷你游戏模式
         */

        result.add(new ArchiveExplanation("迷你游戏_坚果保龄球", "MINI_GAMES_WALL_NUT_BOWLING", USE_REGEX_SIGN + "game\\d+?_17\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_僵尸快跑", "MINI_GAMES_ZOMBIE_NIMBLE_ZOMBIE_QUICK", USE_REGEX_SIGN + "game\\d+?_29\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_小僵尸大麻烦", "MINI_GAMES_BIG_TROUBLE_LITTLE_ZOMBIE", USE_REGEX_SIGN + "game\\d+?_24\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_植物僵尸", "MINI_GAMES_ZOM_BOTANY", USE_REGEX_SIGN + "game\\d+?_16\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_老虎机", "MINI_GAMES_SLOT_MACHINE", USE_REGEX_SIGN + "game\\d+?_18\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_雨中种植物", "MINI_GAMES_ITS_RAINING_SEEDS", USE_REGEX_SIGN + "game\\d+?_19\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_宝石迷阵", "MINI_GAMES_BEGHOULED", USE_REGEX_SIGN + "game\\d+?_20\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_隐形食脑者", "MINI_GAMES_INVISI_GHOUL", USE_REGEX_SIGN + "game\\d+?_21\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_看星星", "MINI_GAMES_SEEING_STARS", USE_REGEX_SIGN + "game\\d+?_22\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_僵尸水族馆", "MINI_GAMES_ZOMBIQUARIUM", USE_REGEX_SIGN + "game\\d+?_23\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_宝石迷阵2", "MINI_GAMES_BEGHOULED_TWIST", USE_REGEX_SIGN + "game\\d+?_24\\.dat"));
//        result.add(new ArchiveExplanation("迷你游戏_传送门", "MINI_GAMES_PORTAL_COMBAT",USE_REGEX_SIGN + "game\\d+?_25\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_保护传送门", "MINI_GAMES_PORTAL_COMBAT", USE_REGEX_SIGN + "game\\d+?_26\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_你看，他们像柱子一样", "MINI_GAMES_COLUMN_LIKE_YOU_SEE_EM", USE_REGEX_SIGN + "game\\d+?_27\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_雪橇区", "MINI_GAMES_BOBSLED_BONANZA", USE_REGEX_SIGN + "game\\d+?_28\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_锤僵尸", "MINI_GAMES_WHACK_A_ZOMBIE", USE_REGEX_SIGN + "game\\d+?_30\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_植物僵尸2", "MINI_GAMES_ZOM_BOTANY_2", USE_REGEX_SIGN + "game\\d+?_32\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_谁笑到最后", "MINI_GAMES_LAST_STAND", USE_REGEX_SIGN + "game\\d+?_31\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_坚果保龄球2", "MINI_GAMES_WALL_NUT_BOWLING_2", USE_REGEX_SIGN + "game\\d+?_33\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_跳跳舞会", "MINI_GAMES_POGO_PARTY", USE_REGEX_SIGN + "game\\d+?_34\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_僵王博士的复仇", "MINI_GAMES_DR_ZOMBOSS_REVENGE", USE_REGEX_SIGN + "game\\d+?_35\\.dat"));

        /**
         * 迷你游戏模式 >> 隐藏游戏
         */
        result.add(new ArchiveExplanation("迷你游戏_坚果保龄球艺术锦标赛", "ART_CHALLENGE_WALL_NUT", USE_REGEX_SIGN + "game\\d+?_36\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_晴天", "MINI_GAMES_SUNNY_DAY", USE_REGEX_SIGN + "game\\d+?_37\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_无草皮之地", "MINI_GAMES_UNSODDED", USE_REGEX_SIGN + "game\\d+?_38\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_重要时间", "MINI_GAMES_BIG_TIME", USE_REGEX_SIGN + "game\\d+?_39\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_挑战种太阳花的艺术", "MINI_GAMES_ART_CHALLENGE_SUNFLOWER", USE_REGEX_SIGN + "game\\d+?_40\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_空袭", "MINI_GAMES_AIR_RAID", USE_REGEX_SIGN + "game\\d+?_41\\.dat"));
        // 注意: 隐藏关_冰冻关卡 没有存档机制
        result.add(new ArchiveExplanation("迷你游戏_超乎寻常的压力", "MINI_GAMES_HIGH_GRAVITY ", USE_REGEX_SIGN + "game\\d+?_44\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_坟墓模式", "MINI_GAMES_GRAVE_DANGER", USE_REGEX_SIGN + "game\\d+?_45\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_你能把它挖出来吗？", "MINI_GAMES_CAN_U_DIG_IT?", USE_REGEX_SIGN + "game\\d+?_46\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_黑暗的暴风雨夜", "MINI_GAMES_DARK_STORMY_NIGHT", USE_REGEX_SIGN + "game\\d+?_47\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_蹦极闪电战", "MINI_GAMES_BUNGEE_BLITZ", USE_REGEX_SIGN + "game\\d+?_48\\.dat"));
        result.add(new ArchiveExplanation("迷你游戏_Squirrel", "MINI_GAMES_SQUIRRE", USE_REGEX_SIGN + "game\\d+?_49\\.dat"));

        /**
         * 解谜模式
         */
        result.add(new ArchiveExplanation("解谜模式_砸罐子_破罐者", "PUZZLE_VASEBREAKER_VASEBREAKER", USE_REGEX_SIGN + "game\\d+?_51\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_全部留下", "PUZZLE_VASEBREAKER_TO_THE_LEFT", USE_REGEX_SIGN + "game\\d+?_52\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_第三个罐子", "PUZZLE_VASEBREAKER_THIRD_VASE", USE_REGEX_SIGN + "game\\d+?_53\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_连锁反应", "PUZZLE_VASEBREAKER_CHAIN_REACTION", USE_REGEX_SIGN + "game\\d+?_54\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_M的意思是金属", "PUZZLE_VASEBREAKER_M_IS_FOR_METAL", USE_REGEX_SIGN + "game\\d+?_55\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_胆怯的制陶工", "PUZZLE_VASEBREAKER_SCARY_POTTER", USE_REGEX_SIGN + "game\\d+?_56\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_变戏法", "PUZZLE_VASEBREAKER_HOKEY_POKEY", USE_REGEX_SIGN + "game\\d+?_57\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_另一个连锁反应", "PUZZLE_VASEBREAKER_ANOTHER_CHAIN_REACTION", USE_REGEX_SIGN + "game\\d+?_58\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_王牌罐子", "PUZZLE_VASEBREAKER_ACE_OF_VASE", USE_REGEX_SIGN + "game\\d+?_59\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_砸罐子_无尽", "PUZZLE_VASEBREAKER_VASEBREAKER_ENDLESS", USE_REGEX_SIGN + "game\\d+?_60\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_我是僵尸", "PUZZLE_I_AM_A_ZOMBIE_ZOMBIE", USE_REGEX_SIGN + "game\\d+?_61\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_我也是僵尸", "PUZZLE_I_AM_A_ZOMBIE_ZOMBIE_TOO", USE_REGEX_SIGN + "game\\d+?_62\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_你能铲了它吗", "PUZZLE_I_AM_A_ZOMBIE_CAN_YOU_DIG_IT", USE_REGEX_SIGN + "game\\d+?_63\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_完全傻了", "PUZZLE_I_AM_A_ZOMBIE_TOTALLY_NUTS", USE_REGEX_SIGN + "game\\d+?_64\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_死亡飞艇", "PUZZLE_I_AM_A_ZOMBIE_DEAD_ZEPPELIN", USE_REGEX_SIGN + "game\\d+?_65\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_我烂了", "PUZZLE_I_AM_A_ZOMBIE_ME_SMASH", USE_REGEX_SIGN + "game\\d+?_66\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_僵尸摇摆", "PUZZLE_I_AM_A_ZOMBIE_ZOM_BOOGIE", USE_REGEX_SIGN + "game\\d+?_67\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_三连击", "PUZZLE_I_AM_A_ZOMBIE_THREE_HIT_WONDER", USE_REGEX_SIGN + "game\\d+?_68\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_你所有的大脑都是属于我的", "PUZZLE_I_AM_A_ZOMBIE_ALL_YOUR_BRAINZ_R_BELONG_TO_US", USE_REGEX_SIGN + "game\\d+?_69\\.dat"));
        result.add(new ArchiveExplanation("解谜模式_我是僵尸_无尽", "PUZZLE_I_AM_A_ZOMBIE_ZOMBIE_ENDLESS", USE_REGEX_SIGN + "game\\d+?_70\\.dat"));


        /**
         * 自定义备份文件
         */
        result.add(new ArchiveExplanation("你的自定义备份文件的注解", "YOUR_CUSTOM_FILE_SIGN", "your_custom_file_name\\.dat"));

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

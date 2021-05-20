package me.japanesestudy.app.wordremember.datasource.data;

import java.util.List;

/**
 * Created by guyu on 2018/1/16.
 */

public class CompileWordUnit extends WordUnit {

    public CompileWordUnit() {
        testUnitWord = new WordList();
        parentId = 0;
        testUnitName = "综合测试";
        this.setNewName(testUnitName);
    }
    public void addTestUnit(AbstractWordUnit abstractWordUnit) {
        testUnitWord.addAll(abstractWordUnit.getTestUnitWord());
        testUnitWord.clearSameWord();
    }
    public static CompileWordUnit getInstance(List<AbstractWordUnit> abstractWordUnits) {
        CompileWordUnit compileTestUnit = new CompileWordUnit();
        for(AbstractWordUnit abstractWordUnit : abstractWordUnits) {
            compileTestUnit.addTestUnit(abstractWordUnit);
        }
        return compileTestUnit;
    }
}

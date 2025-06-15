package de.dicos.springboot.repairservice.restful.model;

public enum RepairAction {
    ACHSENREPARATUR("Achsenreparatur"),
    BATTERIEWECHSEL("Batteriewechsel"),
    BREMSBELAGWECHSEL("Bremsbelagwechsel"),
    BREMSSCHEIBENWECHSEL("Bremsscheibenwechsel"),
    ELEKTRONIKDIAGNOSE("Elektronikdiagnose"),
    FAHRWERKTUNING("Fahrwerkstuning"),
    GETRIEBEREPARATUR("Getriebereparatur"),
    GETRIEBEOELWECHSEL("Getriebeölwechsel"),
    KLIMAANLAGENWARTUNG("Klimaanlagenwartung"),
    KRAFTSTOFFSYSTEMREINIGUNG("Kraftstoffsystemreinigung"),
    KUPPLUNGSUSTAUSCH("Kupplungsaustausch"),
    KUEHLMITTELAUSTAUSCH("Kühlmittelaustausch"),
    LUFTFILTERWECHSEL("Luftfilterwechsel"),
    MOTORABSTIMMUNG("Motorabstimmung"),
    RADAUSRICHTUNG("Radausrichtung"),
    REIFENROTATION("Reifenrotation"),
    STEUERKETTENTAUSCH("Steuerkettentausch"),
    ZAHNRIEMENWECHSEL("Zahnriemenwechsel"),
    ZUENDKERZENWECHSEL("Zündkerzenwechsel"),
    OELWECHSEL("Ölwechsel");

    private final String csvLabel;

    RepairAction(String csvLabel) {
        this.csvLabel = csvLabel;
    }

    public String getCsvLabel() {
        return csvLabel;
    }
}

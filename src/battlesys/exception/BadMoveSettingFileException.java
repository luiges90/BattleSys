package battlesys.exception;

/**
 * Thrown when the move setting file is bad i.e. does not contain data for every move
 * @author Peter
 */
public class BadMoveSettingFileException extends BattleSysRuntimeException{

    public BadMoveSettingFileException(){
        super("Move Setting file is malformed.");
    }


}

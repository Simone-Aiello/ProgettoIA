package main;

import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Rocco{
    private DesktopService service;
    private Handler handler;
    private InputProgram fixedProgram;
    private static Rocco instance = null;
    private InputProgram variableProgram;
    private BoardController controller;
    private Rocco(String exePath, String programPath,BoardController controller){
        service = new DLV2DesktopService(exePath);
        handler= new DesktopHandler(service);
        fixedProgram = new ASPInputProgram();
        fixedProgram.addFilesPath(programPath);
        variableProgram = new ASPInputProgram();
        handler.addProgram(fixedProgram);
        handler.addProgram(variableProgram);
        this.controller = controller;
    }
    public void init(String exePath, String programPath,BoardController controller){
        instance = new Rocco(exePath,programPath,controller);
    }
    public Rocco(){
        try {
            ASPMapper.getInstance().registerClass(Piece.class);
            ASPMapper.getInstance().registerClass(PossibleMoves.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e) {
            e.printStackTrace();
        }
    }

    public static Rocco getInstance(){
        if(instance == null){
            instance = new Rocco();
        }
        return instance;
    }
    public void addFacts(List<PossibleMoves> moves, List<Piece> pieces){
        try {
            variableProgram.clearAll();
            for(PossibleMoves pm : moves){
                variableProgram.addObjectInput(pm);
            }
            for(Piece p : pieces){
                variableProgram.addObjectInput(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String pg = ((ASPInputProgram) variableProgram).getPrograms();
        System.out.println(pg);
    }
    public void startIA(){
        handler.startAsync(controller);
    }
}

package com.njuse.jvmfinal.runtime.struct;

import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.runtime.Vars;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NonArrayObject extends JObject{
    private Vars fields;
//    private ArrayList<Pair<String, Integer>> fieldInfoList;

    public NonArrayObject(JClass clazz) {
        assert clazz != null;
        this.clazz = clazz;
        numInHeap++;
        fields = new Vars(clazz.getInstanceSlotCount());
//        fieldInfoList = new ArrayList<>();
        initObject(clazz);
//        generateInstanceFieldInfoList(clazz);
    }

    private void initObject(JClass clazz){
        do{
            Field[] fields=clazz.getFields();

            for(Field field:fields){
                if(!field.isStatic()){
                    initDefaultValue(field);
//                    String type = parseDescriptor(field.getDescriptor());
//                    String name = field.getName();
//                    int slotID =field.getSlotID();
//                    fieldInfoList.add(Pair.of(type + " " + name, slotID));
                }
            }
            clazz = clazz.getSuperClass();
        }while(clazz != null);
    }

    private void initDefaultValue(Field f){
        String descriptor = f.getDescriptor();
        switch (descriptor.charAt(0)) {
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
            case 'I':
                this.fields.setInt(f.getSlotID(), 0);
                break;
            case 'F':
                this.fields.setFloat(f.getSlotID(), 0);
                break;
            case 'J':
                this.fields.setLong(f.getSlotID(), 0);
                break;
            case 'D':
                this.fields.setDouble(f.getSlotID(), 0);
                break;
            default:
                this.fields.setObjectRef(f.getSlotID(), new NullObject());
                break;
        }
    }
}

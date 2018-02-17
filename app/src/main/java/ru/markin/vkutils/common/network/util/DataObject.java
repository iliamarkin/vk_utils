package ru.markin.vkutils.common.network.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of = {"obj1", "obj2"})
public class DataObject<Obj1, Obj2> {

    @Getter
    @Setter
    private Obj1 obj1;
    @Getter
    @Setter
    private Obj2 obj2;

    public static <Obj1, Obj2> DataObject<Obj1, Obj2> of(final Obj1 obj1, final Obj2 obj2) {
        final DataObject<Obj1, Obj2> dataObject = new DataObject<>();
        dataObject.obj1 = obj1;
        dataObject.obj2 = obj2;
        return dataObject;
    }
}

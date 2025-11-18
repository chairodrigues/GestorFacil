package com.example.gestorfacil.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
 
@Database(entities = {
        Usuario.class,
        Material.class,
        Estoque.class,
        Movimentacao.class
    }, 
    version = 1,  
    exportSchema = false)
@TypeConverters({Converters.class})  
public abstract class AppDatabase extends RoomDatabase {
 
    public abstract UsuarioDao usuarioDao();
    public abstract MaterialDao materialDao();
    public abstract EstoqueDao estoqueDao();
    public abstract MovimentacaoDao movimentacaoDao();
 
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "gestor_facil_db") 
                            .fallbackToDestructiveMigration() 
                            .build();
                }
            }
        }
        return INSTANCE;
    } 
}
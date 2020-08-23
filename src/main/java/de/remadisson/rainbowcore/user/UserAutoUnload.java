package de.remadisson.rainbowcore.user;

import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.sql.Database;
import de.remadisson.rainbowcore.user.instances.User;
import de.remadisson.rainbowcore.velocity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UserAutoUnload {


    public static void UserUpdateAndUnload(ProxyServer server){
        files.pool.execute(() -> {
        server.getScheduler().buildTask(velocity.getInstance(), () -> {
           UserDataAPI api = new UserDataAPI();
           ArrayList<User> delete = new ArrayList<>();
           for(Map.Entry<UUID, User> entry : api.getloadedUsers().entrySet()){

               if(!server.getPlayer(entry.getKey()).isPresent()){
                   delete.add(entry.getValue());
                   try {
                       Database.saveUser(entry.getValue());
                   } catch (SQLException throwables) {
                       throwables.printStackTrace();
                   }
               }

               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }

            for(User user : delete){
                api.unloadUser(user.getUUID());
            }

        }).delay(2, TimeUnit.MINUTES).repeat(20, TimeUnit.MINUTES).schedule();
        });
    }

}

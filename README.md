# mongocbg

This is a mongodb based Spring-boot application. Contains few webservice for our CBG app. Below is the process to install mongo locally and get started with this project.

# MongoDB installation on windows:
1. Install MongoDB Community Server (MSI/Zip) from https://www.mongodb.com/try/download/community
2. Use following commands to create a Superuser.
<p>
<code>
mongo.exe --host <HOSTNAME> --port <PORT>
</code>
<br>
<code>
use admin
</code>
<br>
<code>
db.createUser(
  {
    user: "root",
    pwd: "changeMeToAStrongPassword",
    roles: [ "root" ]
  }
)
</code>
</p>

3. Now `superuser` is created. Verify it with following command: <code> show users </code>
4. Shut down your mongod with following command: <code>db.shutdownServer() </code>


3. In `mongod.cfg` file, remove '#' from line security and add `authorization: enabled` after a new line and tab. Should be like following.
<p>
<code>
security:
</code>
<br>
<code>
&nbsp;&nbsp;&nbsp;&nbsp;authorization: enabled
</code>
</p>

5. Restart your `mongod` using command: <code>mongod -f "C:\Program Files\MongoDB\Server\5.0\bin\mongod.cfg"</code>
6. Connect the `mongod` with command <code>mongo --authenticationDatabase "admin" -u "root" -p</code>
7. If you are using `Compass` to connect with `mongod` then you may use following connection string:
<code>
mongodb://root:changeMeToAStrongPassword@localhost:27017/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false
</code>
8. Create db specific user
<code>
db.createUser({user:"cbg", pwd:"changeMeToAStrongPassword", roles:[{role:"dbOwner", db:"cbg"}]});
</code> 
# Running Springboot application:
  <code>mvn spring-boot:run</code>

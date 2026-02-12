#!

#/usr/local/java/jdk1.8.0_111
nano ~/.bashrc 
source ~/.bashrc 

sudo update-alternatives --install /usr/bin/java java /usr/local/java/jdk1.8.0_111/bin/java 1
sudo update-alternatives --install /usr/bin/jps jps /usr/local/java/jdk1.8.0_111/bin/jps 1
sudo update-alternatives --install /usr/bin/jvisualvm jvisualvm /usr/local/java/jdk1.8.0_111/bin/jvisualvm 1
sudo update-alternatives --install /usr/bin/jconsole jconsole /usr/local/java/jdk1.8.0_111/bin/jconsole 1
sudo update-alternatives --install /usr/bin/jconsole jconsole /usr/local/java/jdk1.8.0_111/bin/java 1
sudo update-alternatives --install /usr/bin/javac javac /usr/local/java/jdk1.8.0_111/bin/javac 1


#----------------------------------------------------------------------------------------------

# install Redis
sudo apt-get update
sudo apt-get install build-essential tcl
cd /tmp
curl -O http://download.redis.io/redis-stable.tar.gz
tar xzvf redis-stable.tar.gz
cd redis-stable
make
make test
sudo make install

sudo mkdir /etc/redis
sudo cp /tmp/redis-stable/redis.conf /etc/redis

sudo nano /etc/redis/redis.conf
#supervised systemd
#dir /var/lib/redis

sudo nano /etc/systemd/system/redis.service
[Unit]
Description=Redis In-Memory Data Store
After=network.target

[Service]
User=redis
Group=redis
ExecStart=/usr/local/bin/redis-server /etc/redis/redis.conf
ExecStop=/usr/local/bin/redis-cli shutdown
Restart=always

[Install]
WantedBy=multi-user.target


sudo adduser --system --group --no-create-home redis
sudo mkdir /var/lib/redis
sudo chown redis:redis /var/lib/redis
sudo chmod 770 /var/lib/redis
sudo systemctl start redis
sudo systemctl status redis

sudo systemctl enable redis

#----------------------------------------------------------------------------------------------

# install Maria
sudo apt-get install software-properties-common
sudo apt-key adv --recv-keys --keyserver hkp://keyserver.ubuntu.com:80 0xF1656F24C74CD1D8
sudo add-apt-repository 'deb [arch=amd64,i386,ppc64el] http://ftp.utexas.edu/mariadb/repo/10.1/ubuntu xenial main'

sudo apt-get update
sudo apt-get install mariadb-server

sudo systemctl start mariadb
sudo systemctl status mariadb

mysql_secure_installation

mysql -u root -p

mysqldump -u user -p db_name > backup-file.sql
mysql -u root -p db_name < backup-file.sql

#----------------------------------------------------------------------------------------------

#ssh password less
ssh-keygen
ssh-copy-id test@chista-test
ssh test@chista-test

ssh-keygen -t rsa
ssh user@jvb mkdir -p .ssh
cat .ssh/id_rsa.pub | ssh user@jvb 'cat >> .ssh/authorized_keys'
ssh user@jvb "chmod 700 .ssh; chmod 640 .ssh/authorized_keys"
ssh user@jvb

#----------------------------------------------------------------------------------------------

nohup java -Xms256m -Xmx512m -jar ---.jar > sth.log 2>&1 &

#----------------------------------------------------------------------------------------------

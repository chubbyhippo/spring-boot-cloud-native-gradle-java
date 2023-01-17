Vagrant.configure("2") do |config|
    config.vm.box = "generic/arch"
    config.vm.provider "virtualbox" do |v|
        v.memory = 5120
        v.cpus = 4
    end
    config.vm.synced_folder ".", "/home/vagrant/dev"
    config.vm.network "forwarded_port", guest: 10350, host: 10350
    config.vm.network "forwarded_port", guest: 9000, host: 8080
    config.vm.network "forwarded_port", guest: 15672, host: 15672
    config.vm.provision "shell", reboot: true, inline: <<-SHELL
        sudo pacman -Syu --noconfirm
        sudo pacman -S --noconfirm docker docker-compose kubectl minikube jdk-openjdk
        sudo usermod -aG docker vagrant
        sudo systemctl enable docker.service
        sudo runuser -l vagrant -c "curl -fsSL https://raw.githubusercontent.com/tilt-dev/tilt/master/scripts/install.sh | bash"
    SHELL
    config.vm.provision "shell", run: "always", inline: <<-SHELL
        export JAVA_HOME=/usr/lib/jvm/default
        echo $JAVA_HOME
#         sudo runuser -l vagrant -c "minikube start"
    SHELL
end
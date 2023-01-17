#!/bin/sh

minikube start --cpus 4 --memory 4g --driver docker
minikube addons enable ingress
minikube tunnel

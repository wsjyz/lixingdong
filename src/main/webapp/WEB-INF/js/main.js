"use strict";
angular.module('ionic.example', ['ionic'])
    .controller('PopupCtrl', function ($scope, $ionicLoading, $timeout, $q, $ionicPopup) {
        $scope.show = function () {
            $ionicLoading.show({
                template: '载入中...'
            });
        };
        $scope.hide = function () {
            $ionicLoading.hide();
        };
        $scope.showAlert = function (content) {
            $ionicPopup.alert({
                title: '提示',
                content: content
            }).then(function (res) {
                console.log('alert done!');
            });
        };
        $scope.showPrompt = function () {
            $ionicPopup.prompt({
                title: '填写价格',
                subTitle: '价格'
            }).then(function (res) {
                console.log('价格是：', res);
            });
        };
    })



(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('AgenceClientDetailController', AgenceClientDetailController);

    AgenceClientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AgenceClient', 'Client'];

    function AgenceClientDetailController($scope, $rootScope, $stateParams, previousState, entity, AgenceClient, Client) {
        var vm = this;

        vm.agenceClient = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:agenceClientUpdate', function(event, result) {
            vm.agenceClient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

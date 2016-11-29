(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('TypeCommandeDetailController', TypeCommandeDetailController);

    TypeCommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeCommande'];

    function TypeCommandeDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeCommande) {
        var vm = this;

        vm.typeCommande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:typeCommandeUpdate', function(event, result) {
            vm.typeCommande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

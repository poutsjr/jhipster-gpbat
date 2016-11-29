(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('CorpsEtatDetailController', CorpsEtatDetailController);

    CorpsEtatDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CorpsEtat'];

    function CorpsEtatDetailController($scope, $rootScope, $stateParams, previousState, entity, CorpsEtat) {
        var vm = this;

        vm.corpsEtat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:corpsEtatUpdate', function(event, result) {
            vm.corpsEtat = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

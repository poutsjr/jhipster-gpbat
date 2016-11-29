(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('UtilisateurDetailController', UtilisateurDetailController);

    UtilisateurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Utilisateur'];

    function UtilisateurDetailController($scope, $rootScope, $stateParams, previousState, entity, Utilisateur) {
        var vm = this;

        vm.utilisateur = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gpbatApp:utilisateurUpdate', function(event, result) {
            vm.utilisateur = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

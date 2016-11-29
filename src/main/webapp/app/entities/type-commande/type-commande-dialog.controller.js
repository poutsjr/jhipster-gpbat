(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('TypeCommandeDialogController', TypeCommandeDialogController);

    TypeCommandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeCommande'];

    function TypeCommandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeCommande) {
        var vm = this;

        vm.typeCommande = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.typeCommande.id !== null) {
                TypeCommande.update(vm.typeCommande, onSaveSuccess, onSaveError);
            } else {
                TypeCommande.save(vm.typeCommande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:typeCommandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .controller('PartenaireDialogController', PartenaireDialogController);

    PartenaireDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Partenaire'];

    function PartenaireDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Partenaire) {
        var vm = this;

        vm.partenaire = entity;
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
            if (vm.partenaire.id !== null) {
                Partenaire.update(vm.partenaire, onSaveSuccess, onSaveError);
            } else {
                Partenaire.save(vm.partenaire, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gpbatApp:partenaireUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

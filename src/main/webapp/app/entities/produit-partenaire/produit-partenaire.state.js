(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('produit-partenaire', {
            parent: 'entity',
            url: '/produit-partenaire?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.produitPartenaire.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/produit-partenaire/produit-partenaires.html',
                    controller: 'ProduitPartenaireController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('produitPartenaire');
                    $translatePartialLoader.addPart('uniteMetrique');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('produit-partenaire-detail', {
            parent: 'entity',
            url: '/produit-partenaire/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.produitPartenaire.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/produit-partenaire/produit-partenaire-detail.html',
                    controller: 'ProduitPartenaireDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('produitPartenaire');
                    $translatePartialLoader.addPart('uniteMetrique');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProduitPartenaire', function($stateParams, ProduitPartenaire) {
                    return ProduitPartenaire.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'produit-partenaire',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('produit-partenaire-detail.edit', {
            parent: 'produit-partenaire-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit-partenaire/produit-partenaire-dialog.html',
                    controller: 'ProduitPartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProduitPartenaire', function(ProduitPartenaire) {
                            return ProduitPartenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('produit-partenaire.new', {
            parent: 'produit-partenaire',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit-partenaire/produit-partenaire-dialog.html',
                    controller: 'ProduitPartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                article: null,
                                libelle: null,
                                unite: null,
                                prix: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('produit-partenaire', null, { reload: 'produit-partenaire' });
                }, function() {
                    $state.go('produit-partenaire');
                });
            }]
        })
        .state('produit-partenaire.edit', {
            parent: 'produit-partenaire',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit-partenaire/produit-partenaire-dialog.html',
                    controller: 'ProduitPartenaireDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProduitPartenaire', function(ProduitPartenaire) {
                            return ProduitPartenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('produit-partenaire', null, { reload: 'produit-partenaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('produit-partenaire.delete', {
            parent: 'produit-partenaire',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/produit-partenaire/produit-partenaire-delete-dialog.html',
                    controller: 'ProduitPartenaireDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProduitPartenaire', function(ProduitPartenaire) {
                            return ProduitPartenaire.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('produit-partenaire', null, { reload: 'produit-partenaire' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
